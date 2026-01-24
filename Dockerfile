# Use official OpenJDK 17 image
#FROM openjdk:17-jdk-slim
FROM azul/zulu-openjdk:17

# Working directory inside container
WORKDIR /app

# Copy the jar built by Maven or Gradle
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose the port the app will run on
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
