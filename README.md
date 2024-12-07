# Trinity Project - Technical Documentation

## Requirements

For the development environment
- `just` recipe runner for development scripts
- `docker` and 'docker-compose' v2

For the staging environment
- `helm` for helm charts
- `minikube` for local k8s development

## Project setup
You'll have to define the `.env` file which is used by the `compose.dev.yml` file for variable substitions.

## 📦🐋 Docker Image Registry
Here’s how the tagging system works:

- 📝 `draft-X` tags: These are builds for your Merge Requests (MRs). The image is built and tagged `draft-X` each time you push to a branch that has an open MR. So there is one image for each MR that is overriden at each push. It can be used to test deployment of an MR.
- 🔧 `dev-SHA` tags: These are builds created on every push, regardless if the branch is an MR. Each tag is based on the commit SHA. It is used by the CI pipeline to share the codebase between jobs.
- 🚢 `prod-SHA` tags: These are builds created for every push to the main branch, tagged with the commit SHA. So understand that after a fresh merge a new version of the image is built. It is used for deployment in production.

Find all images in our registry here: 👉 [Docker Hub Repository](https://hub.docker.com/repositories/silica5518)

## 🎯 Architecture & Technical Stack
We decided to do an **Event Driven Architecture (EDA)** to manage events throughout the application. We believe it responds correctly to the project we are creating.

## What is the project ?

Trinity is a comprehensive retail management system, designed to modernize grocery chain operations through three interconnected components:
1. Customer mobile application
2. Web-based back office & API
3. DevOps infrastructure

Trinity is provided with a back-office, intended to manager of a team and admin of the application to check analytics of products, total products sold, benefits of the day / month / year and so on.
The front-office is designed for clients, providing the best user experience and astonishing interfaces (UX/UI design).

## 🔑 Key Features

1. **Authentication & Authorization**
    - Secure user authentication
    - Role-based access control
    - API security

2. **Inventory Management**
    - Stock tracking
    - Inventory updates
    - Stock alerts

3. **Team Management**
    - User roles and permissions
    - Team performance metrics
    - Activity tracking

4. **Messaging System**
    - Real-time notifications
    - Automated alerts
    - System events handling



### Prerequisites
- Java 21
- PostgreSQL
- RabbitMQ
- Docker & Docker Compose
- Gitlab Runner

### Environment Configuration
Key configurations needed:
- Database connection settings
- RabbitMQ credentials
- Security parameters
- Application-specific configurations

## 📝 Testing Strategy

Our testing approach covers:
- Unit tests for business logic
- Integration tests for APIs
- Security testing
- Performance testing
- Module integration testing
