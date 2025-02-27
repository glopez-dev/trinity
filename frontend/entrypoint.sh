#!/bin/sh
set -e

REQUIRED_VARS="NEXT_PUBLIC_API_URL NEXT_PUBLIC_ENVIRONMENT"

check_env_vars() {
    local missing_vars=""
    for var in $REQUIRED_VARS; do
        if [ -z "$(eval echo \$$var)" ]; then
            missing_vars="$missing_vars $var"
        fi
    done

    if [ -n "$missing_vars" ]; then
        echo "[entrypoint] Warning: Missing required environment variables:" >&2
        echo "$missing_vars" >&2
    fi
}

# Vérifie que node_modules existe et qu'il n'est pas vide, uniquement en développement
build_node_modules_if_missing() {
    if [ "$NEXT_PUBLIC_ENVIRONMENT" = "dev" ]; then
        if [ ! -d "/app/node_modules" ] || [ -z "$(ls -A /app/node_modules)" ]; then
            echo "[entrypoint.sh] Development environment: node_modules directory is missing or empty. Installing dependencies..."
            npm install
        else
            echo "[entrypoint.sh] Development environment: node_modules directory exists and is not empty. Skipping npm install."
        fi
    fi
}

# Vérifie que .next existe et qu'il n'est pas vide en développement
build_if_dev() {
    if [ "$NEXT_PUBLIC_ENVIRONMENT" = "dev" ]; then
        echo "[entrypoint] Environment is 'dev'. Checking and building Next.js..."
        if [ ! -d "/app/.next" ] || [ -z "$(ls -A /app/.next)" ]; then
            echo "[entrypoint.sh] .next directory is missing or empty. Building Next.js..."
            npm run build
        else
            echo "[entrypoint.sh] .next directory exists and is not empty. Skipping build."
        fi
    fi
}

check_env_vars
build_node_modules_if_missing
build_if_dev

echo "[entrypoint.sh] Configuration verified successfully. Starting the application..."
exec npm run "$@"