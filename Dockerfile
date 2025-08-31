# syntax=docker/dockerfile:1

# ===== Build stage =====
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom first to leverage Docker layer caching of dependencies
COPY pom.xml ./
RUN mvn -q -DskipTests dependency:go-offline

# Copy source and build
COPY src ./src
RUN mvn -q -DskipTests package

# ===== Runtime stage =====
FROM eclipse-temurin:17-jre AS runtime

ENV JAVA_OPTS=""
ENV SPRING_PROFILES_ACTIVE="default"
WORKDIR /app

# Copy the fat jar from the build stage (update if artifactId/version changes)
COPY --from=build /app/target/betting-feed-manager-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

# Use exec form so that signals are properly forwarded
ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -jar /app/app.jar"]
