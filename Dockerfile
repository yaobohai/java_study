FROM registry.cn-hangzhou.aliyuncs.com/bohai_repo/openjdk:8u191-jre-alpine3.9

EXPOSE 8080 8443
WORKDIR /opt/app

ADD target/demo-service-*.jar /opt/app/app.jar
ADD docker-entrypoint.sh /opt/app/
RUN chmod +x /opt/app/docker-entrypoint.sh

CMD ["sh", "/opt/app/docker-entrypoint.sh"]
