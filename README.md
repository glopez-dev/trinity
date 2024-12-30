# Trinity Project Documentation

## Overview

Trinity is a comprehensive retail management solution composed of three distinct parts:
- DevOps infrastructure
- Web application with RESTful API
- Mobile application

The project aims to improve customer experience and internal operations for a grocery chain through:
- Enhanced customer purchasing process via an intuitive mobile application
- Optimized product management and sales supervision through a web application
- Streamlined decision-making with key performance indicators visualization

## Documentation Structure

### Agile user-centered Design
- [User Personas](docs/user_personas.md)
- [User Stories](docs/user_stories.md)
- [User Needs Analysis](docs/user_needs.md)
- [User Needs Prioritization Matrix](docs/user_needs_priorization_matrix.md)

### Project Requirements
- [Functional Requirements](docs/functional_requirements.md)
- [Non-Functional Requirements](docs/non_functional_requirements.md)

### Developer Documentation
- [Working with User Stories](docs/developeur/work_with_user_stories.md)
- [Working with Gitlab Issues in Agile](docs/developeur/issues.md)
#### Domain Driven Design
- [Strategic Design](docs/developeur/domain_driven_design/1_strategic_design.md)
- [Tactical Patterns](docs/developeur/domain_driven_design/3_tactical_patterns.md)
- [C4 Diagrams (System Design)](docs/developeur/domain_driven_design/2_c4_diagrams.md)
- Domain Models
    - [User Domain](docs/developeur/domain_driven_design/4_domain_models/user_domain.md)
    - [Product Domain](docs/developeur/domain_driven_design/4_domain_models/product_domain.md)
    - [Catalog Domain](docs/developeur/domain_driven_design/4_domain_models/catalog_domain.md)
    - [Cart Domain](docs/developeur/domain_driven_design/4_domain_models/cart_domain.md) 
    - [Payment Domain](docs/developeur/domain_driven_design/4_domain_models/payment_domain.md)
    - [Analytics Domain](docs/developeur/domain_driven_design/4_domain_models/analytics_domain.md)


### DevOps Documentation
- [Setup Docker DinD Gitlab Runners on a Cloud VM](docs/devops/setup_gitlab_runners.md)
- [Understand the Docker images produced by the CI](docs/devops/docker_images.md)

### Documentation provided by Epitech
- [Project Kickoff Document](docs/project/T-DEV-70x-kickoff.pdf)
- [Complete Project Specification](docs/project/T-DEV-70x-project.pdf)
- [DevOps Kickoff Documentation](docs/devops/T-DEV-701-devOps_kickoff.pdf)
- [DevOps Bootstrap Guide](docs/devops/T-DEV-701-devOps_bootstrap.pdf)


## Key Features

### Web Application & API
- Product stock management with Open Food Facts API integration
- Customer and sales management
- Key performance indicators visualization
- Secured REST API with JWT authentication
- Comprehensive reporting system

### Mobile Application
- User authentication system
- Product barcode scanning
- Shopping cart management
- PayPal payment integration
- Purchase history tracking

### DevOps Infrastructure
- Containerized environments (development and production)
- Automated CI/CD pipeline
- Integrated testing
- Zero-downtime deployment
- Security measures for sensitive data

## Technology Stack

### Backend
- RESTful API with JWT authentication
- PostgreSQL database
- RabbitMQ for message queuing
- Integration with external APIs (PayPal, Open Food Facts)

### Frontend
- Web application built with React.js
- Mobile application built with React Native
- Responsive and intuitive user interfaces

### DevOps
- GitLab CI/CD pipeline
- Docker containerization
- Automated testing and deployment
- Virtual machine runners

## Getting Started

For detailed setup and contribution guidelines, please refer to:
1. DevOps bootstrap guide for infrastructure setup
2. Developer documentation for coding standards and practices
3. Project specifications for detailed requirements

## Contributing

Please read through our developer documentation before making contributions:
- Follow the domain-driven design principles outlined in the documentation
- Review the work with user stories guide
- Check the known issues document for common problems and solutions

## License

[Your License Information Here]

## Contact

[Your Contact Information Here]