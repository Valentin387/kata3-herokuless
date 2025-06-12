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

# Expose the port (default for Spring Boot is 8080, but Railway assigns dynamically)
EXPOSE 8080

# Run the JAR file
CMD ["java", "-Dserver.port=$PORT", "-jar", "build/libs/kata3.jar"]