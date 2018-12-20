FROM gmaslowski/jdk:8

MAINTAINER Greg Maslowski gregmaslowski@gmail.com

VOLUME /tmp
ADD telemetry-users-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=prod -jar /app.jar