deploy ENV: 
    #!/bin/bash
    just destroy "{{ENV}}"
    if [[ "{{ENV}}" == "dev" ]]; then
        echo "Deploying to the development environment..."
        docker-compose \
            -f compose.dev.yml \
            up --detach 
    # elif [[ "{{ENV}}" == "prod" ]]; then
    #     echo "Deploying to the production environment..."
    #     docker-compose \
    #         -f compose.base.yml \
    #         -f compose.prod.yml \
    #         up -d
    else
        echo "Error: Unknown environment '{{ENV}}'. Please specify 'dev' or 'prod'."
        exit 1
    fi

# Bring down the environment
destroy ENV:
    #!/bin/bash
    if [[ "{{ENV}}" == "dev" ]]; then
        echo "Stopping the development environment..."
        docker-compose \
            -f compose.dev.yml \
            down
    # elif [[ "{{ENV}}" == "prod" ]]; then
    #     echo "Stopping the production environment..."
    #     docker-compose \
    #         -f compose.base.yml \
    #         -f compose.prod.yml \
    #         down
    else
        echo "Error: Unknown environment '{{ENV}}'. Please specify 'dev' or 'prod'."
        exit 1
    fi
