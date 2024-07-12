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

# Start Tomcat
CMD ["catalina.sh", "run"]
