FROM gmaslowski/jdk:8

MAINTAINER Greg Maslowski gregmaslowski@gmail.com

VOLUME /tmp
ADD telemetry-auth-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=prod -jar /app.jar