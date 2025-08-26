FROM openjdk:8u191-jre-alpine3.9

ARG JAR_FILE
EXPOSE 8080 9004 8083
WORKDIR /opt/app

ADD ${JAR_FILE} /opt/app/app.jar
ADD docker-entrypoint.sh /opt/app/

CMD ["sh", "/opt/app/docker-entrypoint.sh"]