# Use Java 21 as the base image
FROM eclipse-temurin:21

# Set the working directory
WORKDIR /app

# Copy the built application JAR into the container
COPY target/weather-prediction-0.0.1-SNAPSHOT.jar weather-app.jar

# Expose port 8080 to allow external access
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "weather-app.jar"]