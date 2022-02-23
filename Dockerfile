FROM openjdk:11-jre-slim-buster
ARG JAR

WORKDIR /app
COPY $JAR app.jar

EXPOSE 8181

# health status is determined by the availability of the /health endpoint
HEALTHCHECK --interval=5s --timeout=5s --retries=10 CMD curl --fail -X GET http://localhost:8181/api/check/health || exit 1


ENTRYPOINT java \
    -Djava.security.edg=file:/dev/.urandom \
    -Dedc.fs.config="configuration.properties" \
    -jar edc.jar
