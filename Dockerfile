# Use a JDK base image with Java 21
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Copy source code
COPY src src

# Ensure gradlew is executable
RUN chmod +x gradlew

# Build the application (skip tests to speed up the build)
RUN ./gradlew clean build -x test

# Expose the port (default for Spring Boot is 8080, but Render assigns dynamically)
EXPOSE 8080

# Run the JAR file using shell form to resolve $PORT
CMD ["java", "-jar", "build/libs/kata3-0.0.1-SNAPSHOT.jar"]