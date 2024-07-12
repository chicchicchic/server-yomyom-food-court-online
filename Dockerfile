# Stage 1: Build the application
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application with Tomcat
FROM tomcat:10-jdk17-corretto
COPY --from=build /app/target/foodCourtServerSide-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Copy custom server.xml to the Tomcat configuration directory
COPY server.xml /usr/local/tomcat/conf/server.xml

# Expose port 8082
EXPOSE 8082

# Start Tomcat
CMD ["catalina.sh", "run"]
