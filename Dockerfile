FROM amd64/eclipse-temurin:21-jre-alpine
VOLUME /tmp
ADD target/HandiXike-Backend-Stable.jar HandiXike-Backend-Stable.jar
ENTRYPOINT ["java", "-jar", "-XX:+UseZGC", "-XX:+ZGenerational", "-DsocksProxyPort=10808", "/HandiXike-Backend-Stable.jar"]
