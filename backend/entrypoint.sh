#!/bin/bash
set -euo pipefail

REQUIRED_VARS=(
    "ENV"
    "SPRING_DATASOURCE_URL"
    "SPRING_DATASOURCE_USERNAME"
    "SPRING_DATASOURCE_PASSWORD"
)

validate_env_vars() {
    local missing_vars=()
    for var in "${REQUIRED_VARS[@]}"; do
        if [[ -z "${!var:-}" ]]; then
            missing_vars+=("$var")
        fi
    done
    if [[ ${#missing_vars[@]} -gt 0 ]]; then
        echo "[entrypoint] Warning: Missing required environment variables:" >&2
        printf '%s\n' "${missing_vars[@]}" >&2
    fi
}

check_db_connectivity() {
    local db_host db_port max_retries=30 retry_interval=5
    db_host=$(echo "$SPRING_DATASOURCE_URL" | sed -E 's|jdbc:[^:]+://([^:/]+).*|\1|')
    db_port=$(echo "$SPRING_DATASOURCE_URL" | sed -E 's|jdbc:[^:]+://[^:/]+:?([0-9]+).*|\1|')
    db_port=${db_port:-5432}

    echo "[entrypoint.sh] Checking database connectivity to $db_host:$db_port"
    
    for ((i=1; i<=$max_retries; i++)); do
        if command -v pg_isready &> /dev/null; then
            if pg_isready -h "$db_host" -p "$db_port" -U "$SPRING_DATASOURCE_USERNAME" > /dev/null 2>&1; then
                echo "[entrypoint.sh] Successfully connected to database using pg_isready"
                return 0
            fi
        elif command -v nc &> /dev/null; then
            if nc -z "$db_host" "$db_port" &>/dev/null; then
                echo "[entrypoint.sh] Successfully connected to database using netcat"
                return 0
            fi
        else
            echo "[entrypoint.sh] Warning: Neither 'pg_isready' nor 'nc' is available. Attempting curl..." >&2
            if curl -f --max-time 5 --silent --show-error "http://$db_host:$db_port" > /dev/null 2>&1; then
                echo "[entrypoint.sh] Successfully connected to database using curl"
                return 0
            fi
        fi

        if [[ $i -lt $max_retries ]]; then
            echo "[entrypoint.sh] Attempt $i/$max_retries: Database not ready. Retrying in ${retry_interval}s..."
            sleep "$retry_interval"
        fi
    done

    echo "[entrypoint.sh] Error: Cannot connect to database at $db_host:$db_port after $max_retries attempts" >&2
    exit 1
}

setup_dev_env() {
    echo "[entrypoint.sh] Configuring development environment..."
    export SPRING_DEVTOOLS_RESTART_ENABLED=true
    export SPRING_DEVTOOLS_LIVERELOAD_ENABLED=true
    # export JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
    export SPRING_PROFILES_ACTIVE=dev # This makes the application use the 'application-dev.properties'
}

setup_prod_env() {
    echo "[entrypoint.sh] Configuring production environment..."
    export SPRING_DEVTOOLS_RESTART_ENABLED=false
    export SPRING_PROFILES_ACTIVE=prod
    
    # Set JVM options optimized for containers
    export JAVA_OPTS="${JAVA_OPTS:-} \
        -XX:+UseContainerSupport \
        -XX:MaxRAMPercentage=75.0 \
        -XX:+UseG1GC \
        -XX:+ParallelRefProcEnabled \
        -XX:MaxInlineLevel=20 \
        -XX:+ExitOnOutOfMemoryError \
        -XX:+HeapDumpOnOutOfMemoryError \
        -XX:HeapDumpPath=/tmp/heapdump.hprof \
        -Djava.security.egd=file:/dev/./urandom"

    if [[ ! -f /app/backend/app.jar ]]; then
        echo "[entrypoint.sh] Error: /app/backend/app.jar not found!" >&2
        exit 1
    fi
}

run_spring_boot() {
    echo "[entrypoint.sh] Starting Spring Boot application in ${ENV} environment..."
    case "${ENV}" in
        dev)
            if [[ ! -f ./mvnw ]]; then
                echo "[entrypoint.sh] Error: Maven wrapper './mvnw' not found in /app/backend" >&2
                exit 1
            fi

            if ! command -v inotifywait &> /dev/null; then
                echo "[entrypoint.sh] Error: 'inotifywait' is not installed." >&2
                exit 1
            fi

            inotifywait -r -e modify /app/backend/src/main/ |
            while read -r path action file; do
                echo "[entrypoint.sh] File changed: $path$file. Recompiling..."
                ./mvnw clean compile -o -DskipTests
            done &
            trap 'kill $(jobs -p) 2>/dev/null' EXIT

            exec ./mvnw spring-boot:run
            ;;

        prod)
            exec java ${JAVA_OPTS:-} \
                -jar /app/backend/app.jar
            ;;
        *)
            echo "[entrypoint.sh] Invalid ENV value: ${ENV}. Defaulting to production." >&2
            setup_prod_env
            exec java ${JAVA_OPTS:-} \
                -jar /app/backend/app.jar
            ;;
    esac
}

handle_shutdown() {
    echo "[entrypoint.sh] Received shutdown signal. Initiating graceful shutdown..."
    # Kill any background processes in dev mode
    if [[ "${ENV}" == "dev" ]]; then
        kill $(jobs -p) 2>/dev/null || true
    fi
    exit 0
}

main() {
    # Set up signal handlers for graceful shutdown
    trap handle_shutdown SIGTERM SIGINT

    validate_env_vars
    check_db_connectivity

    case "${ENV}" in
        dev) setup_dev_env ;;
        prod) setup_prod_env ;;
        *) echo "[entrypoint.sh] Invalid ENV value: ${ENV}. Defaulting to production." >&2; setup_prod_env ;;
    esac

    run_spring_boot
}

main