FROM amd64/eclipse-temurin:21-jre-alpine
VOLUME /tmp
ADD target/HandiXike-Backend-0.0.1-SNAPSHOT.jar WebNotes.jar
ENTRYPOINT ["java", "-jar", "/WebNotes.jar"]