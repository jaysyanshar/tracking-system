FROM maven:3.9-amazoncorretto-21 AS build

# Set working directory
WORKDIR /app

# Copy maven configuration
COPY pom.xml .
COPY entity/pom.xml entity/
COPY repository/pom.xml repository/
COPY service/pom.xml service/
COPY rest-api/pom.xml rest-api/

# Download dependencies (this layer will be cached)
RUN mvn dependency:go-offline -B

# Copy source code
COPY entity/src entity/src
COPY repository/src repository/src
COPY service/src service/src
COPY rest-api/src rest-api/src

# Run tests and build the application (will fail if tests don't pass)
RUN mvn clean package

# Runtime stage
FROM amazoncorretto:21-alpine

# Set working directory
WORKDIR /app

# Copy built artifact from the build stage
COPY --from=build /app/rest-api/target/rest-api-1.0-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
