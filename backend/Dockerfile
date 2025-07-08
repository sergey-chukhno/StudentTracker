# Use OpenJDK 17 as the base image for building
FROM maven:3.9.6-eclipse-temurin-17 as build

WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Build the fat JAR
RUN mvn package

# Use a smaller JRE image for running the app
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the fat JAR from the build stage
COPY --from=build /app/target/StudentTracker-1.0-SNAPSHOT.jar ./StudentTracker.jar

# Expose the port (Render will set $PORT)
EXPOSE 8080

# Start the app
CMD ["sh", "-c", "java -jar StudentTracker.jar"] 