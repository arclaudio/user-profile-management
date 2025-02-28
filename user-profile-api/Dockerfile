# Use OpenJDK 22 as base image
FROM eclipse-temurin:22-jdk AS build

# Set working directory inside the container
WORKDIR /app

# Copy Maven wrapper files (if you use one)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies to speed up builds
RUN ./mvnw dependency:go-offline

# Copy the rest of the application source code
COPY src ./src

# Build the application
RUN ./mvnw package -DskipTests

# Use a minimal JRE image for running the app
FROM eclipse-temurin:22-jre AS runtime

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
