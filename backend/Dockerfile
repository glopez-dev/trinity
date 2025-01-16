#########
# BUILD #
#########

# The first stage is responsible for only resolving dependencies and acts as a kind of cache.
# This way, when the source code changes but the pom.xml file remains unchanged,
# we don’t have to wait for the dependencies to be downloaded again.

FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app/backend

# Arguments pour personnaliser le build
ARG SKIP_TESTS=true
ENV MAVEN_OPTS="-Xmx2g -DthreadCount=4"

# Copie des fichiers de dépendances
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Télécharger toutes les dépendances
RUN chmod +x mvnw && \
    ./mvnw -B dependency:go-offline -DskipTests

# Copie du code source
COPY src src

# Construction conditionnelle des tests
RUN if [ "$SKIP_TESTS" = "false" ]; then \
        ./mvnw -B verify; \
    else \
        ./mvnw -B package -DskipTests; \
    fi

##############
# PRODUCTION #
##############

# Java Runtime Environment (JRE)
# Doesn't includes the development tools
FROM eclipse-temurin:21-jre-jammy AS prod

WORKDIR /app/backend

EXPOSE 8080

ENV ENV="prod"

# Copie du package compilé
COPY --from=build /app/backend/target/*.jar /app/backend/app.jar

# Copie du script de lancement du conteneur
COPY entrypoint.sh /entrypoint.sh

# Modification des permissions
RUN  chmod 550 /entrypoint.sh

# Utilisateur non-root pour la sécurité
RUN addgroup --system appgroup && \
    adduser --system --ingroup appgroup appuser

USER appuser

ENTRYPOINT ["/entrypoint.sh"]

HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=5 \
  CMD ["curl", "-f", "http://localhost:8080/actuator/health"]

###############
# DEVELOPMENT #
###############

# Java Development Kit (JDK)
FROM eclipse-temurin:21-jdk-jammy AS dev

WORKDIR /app/backend

EXPOSE 8080

ENV ENV="dev"

# Update and install dependencies
RUN apt update && apt install -y inotify-tools curl && apt-get clean && rm -rf /var/lib/apt/lists/*

# Copy the startup script
COPY entrypoint.sh /app/entrypoint.sh

# Copy Maven wrapper and project definition files
COPY mvnw pom.xml /app/backend/
COPY .mvn .mvn/

# Modify permissions for executables
RUN chmod 755 /app/entrypoint.sh /app/backend/mvnw

# Set volume for hot-reloadable source code
VOLUME /app/backend/src
VOLUME /app/backend/target

# Test command to fake migration into a database
CMD ["mvn", "clean", "initialize", "flyway:migrate", "-Pci", "test"]

# Entry point for the container
ENTRYPOINT ["/app/entrypoint.sh"]

# Healthcheck for application
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=5 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
