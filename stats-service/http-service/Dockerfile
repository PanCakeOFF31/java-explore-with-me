FROM amazoncorretto:11-alpine-jdk
COPY target/"http-service-0.0.1-SNAPSHOT.jar" http-service-app.jar
ENTRYPOINT ["java", "-jar", "/http-service-app.jar"]
