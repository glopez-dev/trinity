#!/bin/bash

# Strict mode: exit on error, unset variables, and on any command in a pipeline failing
set -euo pipefail

# Environment Variables to Check
REQUIRED_VARS=(
    "SPRING_DATASOURCE_URL"
    "SPRING_DATASOURCE_USERNAME"
    "SPRING_DATASOURCE_PASSWORD"
)

# Validation Function
validate_env_vars() {
    local missing_vars=()

    for var in "${REQUIRED_VARS[@]}"; do
        if [[ -z "${!var:-}" ]]; then
            missing_vars+=("$var")
        fi
    done

    if [[ ${#missing_vars[@]} -gt 0 ]]; then
        echo "[entrypoint] Error: Missing required environment variables:" >&2
        printf '%s\n' "${missing_vars[@]}" >&2
        exit 1
    fi
}

# Optional: Additional Pre-startup Checks
pre_startup_checks() {
    # Add any additional startup checks here
    # For example, check database connectivity
    echo "[entrypoint.sh] Performing pre-startup checks..."
    # You could add custom logic to validate database connection
}

# Main Entrypoint
main() {
    echo "[entrypoint.sh] Starting Spring Boot application..."

    # Validate Required Environment Variables
    validate_env_vars

    # Perform Pre-startup Checks
    pre_startup_checks

    # Start Spring Boot Application
    echo "[entrypoint.sh] Launching Spring Boot Application..."
    exec java \
        -XX:+UseContainerSupport \
        -XX:MaxRAMPercentage=75.0 \
        -jar /app/backend/app.jar
}

# Run the main function
main