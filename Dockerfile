FROM adoptopenjdk/openjdk11:alpine-jre

COPY ./target/backend-latest.jar /root/app.jar
COPY ./entrypoint.sh /root/entrypoint.sh

RUN ["chmod", "+x", "/root/entrypoint.sh"]

ENTRYPOINT ["/root/entrypoint.sh"]