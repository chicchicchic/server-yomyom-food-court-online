# Use a Maven image to build the project
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
# Build the project and package it as a WAR file
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/demo-0.0.1-SNAPSHOT.war demo.war
EXPOSE 8082
ENTRYPOINT ["java", "-war", "demo.war"]