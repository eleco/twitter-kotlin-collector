FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/twitter-link-collector-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT exec java -Xmx512m -Xss256k  -Djava.security.egd=file:/dev/./urandom -jar /app.jar
