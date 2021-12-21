FROM openjdk:11-jdk-slim-buster
COPY build/libs/auction.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
