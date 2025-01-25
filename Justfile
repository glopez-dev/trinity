# Bring up a given deployment
up ENV: 
    #!/bin/bash

    just down "{{ENV}}"

    echo "Removing unused docker data..."
    docker system prune --volumes --force # Means no prompt

    if [[ "{{ENV}}" == "dev" ]]; then
        echo "Deploying to the development environment..."
        docker compose --file compose.yml --profile development up --detach 
    elif [[ "{{ENV}}" == "prod" ]]; then
        echo "Deploying to the production environment..."
        docker compose --file compose.yml --profile production pull
        docker compose --file compose.yml --profile production up --detach
    else
        echo "Error: Unknown environment '{{ENV}}'. Please specify 'dev' or 'prod'."
        exit 1
    fi

# Bring down a given deployment 
down ENV:
    #!/bin/bash
    if [[ "{{ENV}}" == "dev" ]]; then
        echo "Stopping the development environment..."
        docker compose --file compose.yml --profile development down
    elif [[ "{{ENV}}" == "prod" ]]; then
        echo "Stopping the production environment..."
        docker compose --file compose.yml --profile production down
    else
        echo "Error: Unknown environment '{{ENV}}'. Please specify 'dev' or 'prod'."
        exit 1
    fi

# Display the configuration for a given profile 
config ENV:
    #!/bin/bash
    if [[ "{{ENV}}" == "dev" ]]; then
        echo "Here's the 'development' compose configuration :"
        docker compose --profile development config 
    elif [[ "{{ENV}}" == "prod" ]]; then
        echo "Here's the 'development' compose configuration :"
        docker compose --profile production config 
    else
        echo "Error: Unknown environment '{{ENV}}'. Please specify 'dev' or 'prod'."
        exit 1
    fi


install_docker_dind_runner:
    docker run \
        --detach \
        --name gitlab-runner-medium \
        --restart always \
        -v /srv/gitlab-runner/config:/etc/gitlab-runner \
        -v  /var/run/docker.sock:/var/run/docker.sock \
        gitlab/gitlab-runner:alpine

register_docker_dind_runner:
    docker run \
    --rm \
    -t \
    -i \
    -v /srv/gitlab-runner/config:/etc/gitlab-runner \
    gitlab/gitlab-runner:alpine \
    register \
        --non-interactive \
        --url "https://t-dev.epitest.eu" \
        --registration-token $TOKEN \
        --executor "docker" \
        --docker-image docker:stable \
        --description "[${NAME}] Local Docker DinD runner" \
        --docker-privileged \
        --docker-tlsverify \
        --docker-volumes "/certs/client" \
        --tag-list "docker-dind" \
        --run-untagged="false"

# Run a shell in the given container of the given environment(dev|prod) and project(backend|frontend)
sh ENV PROJECT:
    docker exec -it trinity-{{PROJECT}}_{{ENV}}-1 sh

# Run the tests for the given project (backend|frontend)
test PROJECT:
    #!/bin/bash
    if [[ "{{PROJECT}}" == "backend" ]]; then
        docker exec -it trinity-backend_dev-1 ./mvnw clean test
    elif [[ "{{PROJECT}}" == "frontend" ]]; then
        docker exec -it trinity-frontend_dev-1 npm run test
    else
        echo "Error: Unknown project '{{PROJECT}}'. Please specify 'backend' or 'frontend'."
        exit 1
    fi