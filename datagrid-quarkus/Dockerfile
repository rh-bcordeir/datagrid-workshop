# Multi-stage Dockerfile for OpenShift deployment of the Quarkus app (JVM mode).
#
# Build:   docker build -t datagrid-quarkus .
# Run:     docker run -i --rm -p 8080:8080 datagrid-quarkus

## Stage 1 — build with Maven
FROM registry.access.redhat.com/ubi9/openjdk-21:1.23 AS build

USER 0
WORKDIR /build

COPY --chown=185 mvnw mvnw.cmd pom.xml ./
COPY --chown=185 .mvn .mvn
RUN ./mvnw -B dependency:go-offline -DskipTests || true

COPY --chown=185 src src
RUN ./mvnw -B package -DskipTests

## Stage 2 — runtime
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.23

ENV LANGUAGE='en_US:en' \
    JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager" \
    JAVA_APP_JAR="/deployments/quarkus-run.jar"

COPY --from=build --chown=185:0 /build/target/quarkus-app/lib/      /deployments/lib/
COPY --from=build --chown=185:0 /build/target/quarkus-app/*.jar     /deployments/
COPY --from=build --chown=185:0 /build/target/quarkus-app/app/      /deployments/app/
COPY --from=build --chown=185:0 /build/target/quarkus-app/quarkus/  /deployments/quarkus/

# OpenShift runs containers with an arbitrary UID in group 0 — make /deployments group-writable.
RUN chgrp -R 0 /deployments && chmod -R g=u /deployments

EXPOSE 8080
USER 185

ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]
