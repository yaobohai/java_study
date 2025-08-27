FROM openjdk:8u191-jre-alpine3.9

EXPOSE 8080 8083
WORKDIR /opt/app

ARG JAR_FILE

WORKDIR /opt/app

ADD ${JAR_FILE} /opt/app/app.jar
ADD docker-entrypoint.sh /opt/app/

CMD ["sh", "/opt/app/docker-entrypoint.sh"]