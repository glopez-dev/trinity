# Bring up a given deployment
up ENV: 
    #!/bin/bash

    just destroy "{{ENV}}"

    echo "Removing unused docker data..."
    docker system prune --force # Means no prompt

    if [[ "{{ENV}}" == "dev" ]]; then
        echo "Deploying to the development environment..."
        docker-compose --file compose.yml --profile development up --detach 
    elif [[ "{{ENV}}" == "prod" ]]; then
        echo "Deploying to the production environment..."
        docker-compose --file compose.yml --profile production pull
        docker-compose --file compose.yml --profile production up --detach
    else
        echo "Error: Unknown environment '{{ENV}}'. Please specify 'dev' or 'prod'."
        exit 1
    fi

# Bring down a given deployment 
down ENV:
    #!/bin/bash
    if [[ "{{ENV}}" == "dev" ]]; then
        echo "Stopping the development environment..."
        docker-compose --file compose.yml --profile development down
    elif [[ "{{ENV}}" == "prod" ]]; then
        echo "Stopping the production environment..."
        docker-compose --file compose.yml --profile production down
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