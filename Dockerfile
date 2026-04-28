FROM maven:3.9-eclipse-temurin-17 AS Build
WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN ./mvnw dependency:go-offline

COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
RUN mkdir -p /app/videoFolder

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]