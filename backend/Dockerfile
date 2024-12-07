FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app/backend
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean package

FROM eclipse-temurin:21-jre-jammy AS prod

EXPOSE 8080

WORKDIR /app/backend

COPY --from=build /app/backend/target/*.jar /app/backend/app.jar

COPY entrypoint.prod.sh /entrypoint.sh

RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]


FROM eclipse-temurin:21-jdk-jammy AS dev
WORKDIR /app/backend
COPY --from=build /app/backend ./
VOLUME /app/backend/src
EXPOSE 8080
CMD ["./mvnw", "spring-boot:run"]