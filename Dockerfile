FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy the built jar (you MUST build with Maven before this)
COPY target/practise-cassandra-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
