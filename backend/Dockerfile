#########
# BUILD #
#########

#########################################################################################
## The first stage is responsible for only resolving dependencies and acts as a kind of
## cache. This way, when the source code changes but the pom.xml file remains unchanged,
## we don’t have to wait for the dependencies to be downloaded again.
#########################################################################################
FROM eclipse-temurin:25-jdk-jammy AS build
WORKDIR /app/backend

#########################################################################################
## Arguments to customiez the Maven build
#########################################################################################
ARG SKIP_TESTS=true
ENV MAVEN_OPTS="-Xmx2g -DthreadCount=4"

#########################################################################################
## Copy the dependency files
#########################################################################################
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

#########################################################################################
## Download all the dependencies
#########################################################################################
RUN chmod +x mvnw && \
    ./mvnw -B dependency:go-offline -DskipTests

#########################################################################################
## Copy the source code
#########################################################################################
COPY src src


#########################################################################################
## Conditional execution of tests
#########################################################################################
RUN if [ "$SKIP_TESTS" = "false" ]; then \
        ./mvnw -B verify; \
    else \
        ./mvnw -B package -DskipTests; \
    fi

##############
# PRODUCTION #
##############

#########################################################################################
## Java Runtime Environment (JRE)
# The JRE is the runtime portion of Java software, it doesn't include the development tools.
#########################################################################################
FROM eclipse-temurin:25-jre-jammy AS prod
WORKDIR /app/backend
EXPOSE 8080
ENV ENV="prod"

#########################################################################################
# Install postgresql-client and curl for healthcheck
#########################################################################################
RUN apt-get update && \
    apt-get install -y --no-install-recommends postgresql-client curl && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

COPY --from=build /app/backend/target/*.jar /app/backend/app.jar

COPY entrypoint.sh /entrypoint.sh

#########################################################################################
## Use a non-root user for security
#########################################################################################
RUN addgroup --system appgroup && \
    adduser --system --ingroup appgroup appuser && \
    chown -R appuser:appgroup /app/backend && \
    chmod 550 /entrypoint.sh && \
    chown appuser:appgroup /entrypoint.sh

USER appuser

ENTRYPOINT ["/entrypoint.sh"]
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=5 \
  CMD ["curl", "-f", "http://localhost:8080/actuator/health"]

###############
# DEVELOPMENT #
###############

#########################################################################################
## Java Development Kit (JDK)
#########################################################################################
FROM eclipse-temurin:25-jdk-jammy AS dev
WORKDIR /app/backend
EXPOSE 8080
ENV ENV="dev"

#########################################################################################
## Update and install inotify-tools and curl
#########################################################################################
RUN apt-get update && apt-get install -y --no-install-recommends inotify-tools postgresql-client curl && apt-get clean && rm -rf /var/lib/apt/lists/*

#########################################################################################
## Copy the startup script
#########################################################################################
COPY entrypoint.sh /app/entrypoint.sh

#########################################################################################
## Copy Maven wrapper and project definition files
#########################################################################################
COPY mvnw pom.xml /app/backend/
COPY .mvn .mvn/

#########################################################################################
## Modify the permissions of the startup script and Maven wrapper
#########################################################################################
RUN chmod 755 /app/entrypoint.sh /app/backend/mvnw

#########################################################################################
## Set voume for hot reload of the source code
#########################################################################################
VOLUME /app/backend/src

ENTRYPOINT ["/app/entrypoint.sh"]

HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=5 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1