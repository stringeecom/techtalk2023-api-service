FROM bitnami/java:20.0.2-10-debian-11-r22

WORKDIR app/
COPY target/stringee-video-demo-1.0.jar ./stringee-video-demo-1.0.jar
COPY target/lib ./lib
COPY config ./config

CMD ["java", "-Dfile.encoding=UTF8", "-Dspring.config.location=config/application.properties", "-jar", "stringee-video-demo-1.0.jar"]