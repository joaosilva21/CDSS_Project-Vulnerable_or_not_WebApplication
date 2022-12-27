FROM openjdk:17-jdk-alpine
ADD demo-0.0.1-SNAPSHOT.jar application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]

COPY rockyou.txt /