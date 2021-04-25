FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/PhotoAppApiZuulAPIGateway-0.0.1-SNAPSHOT.jar PhotoAppApiZuulAPIGateway.jar
ENTRYPOINT ["java", "-jar", "PhotoAppApiZuulAPIGateway.jar"]