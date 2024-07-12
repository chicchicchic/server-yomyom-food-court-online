# Use an official Tomcat runtime as a parent image
FROM tomcat:10.1.10-jdk17

# Set the working directory inside the container
WORKDIR /usr/local/tomcat

# Copy the WAR file to the container
COPY target/foodCourtServerSide-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Copy custom server.xml to the Tomcat configuration directory
COPY server.xml /usr/local/tomcat/conf/server.xml

# Expose port 8082
EXPOSE 8082

# Set the environment variables
ENV MYSQL_HOST=localhost
ENV MYSQL_PORT=3306
ENV MYSQL_DATABASE=food_court_v1
ENV MYSQL_USER=root
ENV MYSQL_PASSWORD=khanh123root
ENV KEYCLOAK_CLIENT_ID=food-court-rest-api
ENV KEYCLOAK_ISSUER_URI=http://localhost:8080/realms/FoodCourtSpringBootKeycloak
ENV KEYCLOAK_JWK_SET_URI=http://localhost:8080/realms/FoodCourtSpringBootKeycloak/protocol/openid-connect/certs

# Start Tomcat
CMD ["catalina.sh", "run"]

