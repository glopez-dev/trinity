FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app/backend
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy AS prod
WORKDIR /app/backend
COPY --from=build /app/backend/target/*.jar /app/backend/*.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/backend/*.jar"]

FROM eclipse-temurin:21-jdk-jammy AS dev
WORKDIR /app/backend
COPY --from=build /app/backend ./
VOLUME /app/backend/src
EXPOSE 8080
CMD ["./mvnw", "spring-boot:run"]