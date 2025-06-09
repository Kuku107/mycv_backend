#FROM openjdk:17
#
#ARG JAR_FILE=target/*.jar
#
#COPY ${JAR_FILE} backend-service.jar
#
#ENTRYPOINT ["java", "-jar", "backend-service.jar"]
#
#EXPOSE 8080

# Stage 1: build
FROM maven:3.9.6-eclipse-temurin-17 as builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: run
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
