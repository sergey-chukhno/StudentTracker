# Use OpenJDK 17 as the base image for building
FROM maven:3.9.6-eclipse-temurin-17 as build

WORKDIR /app

# Copy the entire repo (so Maven can see the parent POM and all modules)
COPY . .

# Build the fat JAR for the backend module
RUN mvn -pl backend -am -DskipTests package

# Use a smaller JRE image for running the app
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the fat JAR from the build stage
COPY --from=build /app/backend/target/StudentTracker-1.0-SNAPSHOT.jar ./StudentTracker.jar

# Expose the port (Render will set $PORT)
EXPOSE 8080

# Start the app
CMD ["sh", "-c", "java -jar StudentTracker.jar"] 