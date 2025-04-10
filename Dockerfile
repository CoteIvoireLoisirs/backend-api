# Use the official OpenJDK 17 image as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files to the container
RUN mvn install && cp target/ci-explore-0.0.1-SNAPSHOT.war app.war

# Check if the WAR file exists, else build it using Maven
COPY target/ci-explore-0.0.1-SNAPSHOT.war app.war

# Expose the port the application will run on
EXPOSE 8086

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "app.war"]