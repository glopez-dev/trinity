#!/bin/bash
set -euo pipefail

REQUIRED_VARS=(
    "ENV"
    "SPRING_APP_NAME"
    "SPRING_DATASOURCE_URL"
    "SPRING_DATASOURCE_USERNAME"
    "SPRING_DATASOURCE_PASSWORD"
    "SPRING_JWT_SECRET"
    "SPRING_JWT_EXPIRATION"
    "SPRING_CORS_ALLOWED_ORIGINS"
)

validate_env_vars() {
    local missing_vars=()
    
    echo "[entrypoint] Debugging environment variables:"
    echo "----------------------------------------"
    echo "Required variables and their values:"
    for var in "${REQUIRED_VARS[@]}"; do
        printf "%-25s = %s\n" "$var" "${!var:-<NOT SET>}"
        if [[ -z "${!var:-}" ]]; then
            missing_vars+=("$var")
            return 1
        fi
    done
    
    echo "----------------------------------------"
    echo "All Spring-related environment variables:"
    env | grep -i "spring" | sort
    echo "----------------------------------------"
    
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

    echo "[entrypoint] Checking database connectivity to $db_host:$db_port"
    
    for ((i=1; i<=$max_retries; i++)); do
        if command -v pg_isready &> /dev/null; then
            if pg_isready -h "$db_host" -p "$db_port" -U "$SPRING_DATASOURCE_USERNAME" > /dev/null 2>&1; then
                echo "[entrypoint] Successfully connected to database using pg_isready"
                return 0
            fi
        elif command -v nc &> /dev/null; then
            if nc -z "$db_host" "$db_port" &>/dev/null; then
                echo "[entrypoint] Successfully connected to database using netcat"
                return 0
            fi
        else
            echo "[entrypoint] Warning: Neither 'pg_isready' nor 'nc' is available. Attempting curl..." >&2
            if curl -f --max-time 5 --silent --show-error "http://$db_host:$db_port" > /dev/null 2>&1; then
                echo "[entrypoint] Successfully connected to database using curl"
                return 0
            fi
        fi

        if [[ $i -lt $max_retries ]]; then
            echo "[entrypoint] Attempt $i/$max_retries: Database not ready. Retrying in ${retry_interval}s..."
            sleep "$retry_interval"
        fi
    done

    echo "[entrypoint] Error: Cannot connect to database at $db_host:$db_port after $max_retries attempts" >&2
    exit 1
}

check_db_authentication() {
    local db_host db_port db_name max_retries=5 retry_interval=5 error_output
    db_host=$(echo "$SPRING_DATASOURCE_URL" | sed -E 's|jdbc:[^:]+://([^:/]+).*|\1|')
    db_port=$(echo "$SPRING_DATASOURCE_URL" | sed -E 's|jdbc:[^:]+://[^:/]+:?([0-9]+).*|\1|')
    db_name=$(echo "$SPRING_DATASOURCE_URL" | sed -E 's|jdbc:[^:]+://[^/]+/([^?]*).*|\1|')
    db_port=${db_port:-5432}

    echo "[entrypoint] Checking database authentication for user $SPRING_DATASOURCE_USERNAME to $db_host:$db_port/$db_name"
    
    for ((i=1; i<=$max_retries; i++)); do
        # Capture both stdout and stderr in error_output variable
        error_output=$(PGPASSWORD="$SPRING_DATASOURCE_PASSWORD" psql -h "$db_host" -p "$db_port" -U "$SPRING_DATASOURCE_USERNAME" -d "$db_name" -c "SELECT 1" 2>&1)
        
        if [ $? -eq 0 ]; then
            echo "[entrypoint] Successfully authenticated to database with provided credentials"
            return 0
        else
            echo "[entrypoint] Attempt $i/$max_retries: Authentication failed. Retrying in ${retry_interval}s..."
            if [[ $i -lt $max_retries ]]; then
                sleep "$retry_interval"
            fi
        fi
    done

    echo "[entrypoint] ERROR: Authentication failed for user $SPRING_DATASOURCE_USERNAME" >&2
    echo "[entrypoint] Please check that:"
    echo "  1. The user $SPRING_DATASOURCE_USERNAME exists in the database"
    echo "  2. The password is correct"
    echo "  3. The user has appropriate permissions"
    
    # Display the error information (filtering out any password details)
    echo "[entrypoint] Detailed error information:"
    echo "$error_output" | grep -v "password"
    
    exit 1
}

setup_dev_env() {
    echo "[entrypoint] Configuring development environment..."
    export SPRING_DEVTOOLS_RESTART_ENABLED=true
    export SPRING_DEVTOOLS_LIVERELOAD_ENABLED=true
    # export JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
    export SPRING_PROFILES_ACTIVE=dev # This makes the application use the 'application-dev.properties'
}

setup_prod_env() {
    echo "[entrypoint] Configuring production environment..."
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
        echo "[entrypoint] Error: /app/backend/app.jar not found!" >&2
        exit 1
    fi
}

run_spring_boot() {
    echo "[entrypoint] Starting Spring Boot application in ${ENV} environment..."
    case "${ENV}" in
        dev)
            if [[ ! -f ./mvnw ]]; then
                echo "[entrypoint] Error: Maven wrapper './mvnw' not found in /app/backend" >&2
                exit 1
            fi

            if ! command -v inotifywait &> /dev/null; then
                echo "[entrypoint] Error: 'inotifywait' is not installed." >&2
                exit 1
            fi

            inotifywait -r -e modify /app/backend/src/main/ |
            while read -r path action file; do
                echo "[entrypoint] File changed: $path$file. Recompiling..."
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
            echo "[entrypoint] Invalid ENV value: ${ENV}. Defaulting to production." >&2
            setup_prod_env
            exec java ${JAVA_OPTS:-} \
                -jar /app/backend/app.jar
            ;;
    esac
}

handle_shutdown() {
    echo "[entrypoint] Received shutdown signal. Initiating graceful shutdown..."
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
    check_db_authentication

    case "${ENV}" in
        dev) setup_dev_env ;;
        prod) setup_prod_env ;;
        *) echo "[entrypoint] Invalid ENV value: ${ENV}. Defaulting to production." >&2; setup_prod_env ;;
    esac

    run_spring_boot
}

main