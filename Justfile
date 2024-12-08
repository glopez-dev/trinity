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
        --description "[Gabriel] Local Docker DinD runner" \
        --docker-privileged \
        --docker-tlsverify \
        --docker-volumes "/certs/client" \
        --tag-list "docker-dind" \
        --run-untagged="false"