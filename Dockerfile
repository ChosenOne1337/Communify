FROM adoptopenjdk/openjdk11:alpine-jre

ARG JAR_FILE

RUN mkdir -p /apps
COPY ./target/backend-0.0.1-SNAPSHOT-spring-boot.jar /apps/app.jar
COPY ./entrypoint.sh /apps/entrypoint.sh

RUN chmod +x /apps/entrypoint.sh
CMD ["/apps/entrypoint.sh"]