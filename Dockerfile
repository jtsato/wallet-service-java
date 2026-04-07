# Java 25 LTS (Eclipse Temurin JRE)
FROM eclipse-temurin:25-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the executable jar file from the target folder to the working directory
COPY configuration/target/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8081

# Define the command to run the application
CMD ["java", "-jar", "app.jar"]
