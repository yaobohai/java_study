FROM openjdk:8u191-jre-alpine3.9

EXPOSE 8080 8083
WORKDIR /opt/app

ADD demo-service-*.jar /opt/app/app.jar

ADD docker-entrypoint.sh /opt/app/

CMD ["sh", "/opt/app/docker-entrypoint.sh"]