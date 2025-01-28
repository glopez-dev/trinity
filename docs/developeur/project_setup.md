# Project Setup

## Prerequisites

Before starting with the project setup, ensure you have the following tools installed:

- Docker and Docker Compose
- Just command runner
- Git

## Installing Just Command Runner

### On Linux/macOS
```bash
# Using Homebrew (macOS and Linux)
brew install just

# Using apt (Debian/Ubuntu)
apt install just
```

## Environment Configuration

1. Create a `.env` file in the project root:
```bash
cp .env.example .env
```

> **Be careful** the file should be named exactly `.env` and nothing else to be used by the `compose.yml` file for interpolation.

2. Configure the following variables in your `.env` file:

```env
# Database Configuration
POSTGRES_DB=mydatabase
POSTGRES_PASSWORD=secret
POSTGRES_USER=myuser

# RabbitMQ Configuration
RABBITMQ_HOST=rabbitmq
RABBITMQ_DEFAULT_PASS=secret
RABBITMQ_DEFAULT_USER=myuser

# API Configuration
NEXT_PUBLIC_API_URL=http://host.docker.internal:8080
```

## Available Just Commands

The following commands are available through the Justfile:

```bash
just -l                                  # List all available commands
just config ENV                         # Display the configuration for a given profile
just up ENV                            # Bring up a given deployment
just down ENV                          # Bring down a given deployment
```

### Command Usage Examples

1. Start the development environment:
```bash
just up dev
```

2. View the configuration for development:
```bash
just config dev
```

3. Stop the development environment:
```bash
just down dev
```

## Development Environment Setup

1. Clone the repository:
```bash
git clone git@t-dev.epitest.eu:PAR_11/T-DEV-702-Api.git
cd T-DEV-702-Api
```

2. Set up the environment file:
```bash
cp .env.example .env
```

3. Start the development environment:
```bash
just up dev
```

## Troubleshooting

### Common Issues

1. **Port Conflicts**
   - Check if ports 8080, 5432, or 5672 are already in use
   - Stop conflicting services or modify the port mappings

2. **Docker Network Issues**
   - Ensure Docker is running
   - Check network connectivity between containers: `docker network ls`

3. **Environment Configuration**
   - Verify all required variables are set in your `.env` file
   - Check for any typos in service names or passwords

### Viewing Logs

To view logs for troubleshooting:

```bash
# View logs for all services
docker-compose logs

# View logs for a specific service
docker-compose logs [service-name]
```

## Next Steps

TODO

## Getting Help

If you encounter any issues not covered in this guide:

1. Check the [Known Issues](docs/developeur/issues.md) documentation
2. Review the project's GitLab issues
3. Contact the development team