# Sping demo

## build for docker

### add properties

```xml
    <properties>
        <java.version>1.8</java.version>
        <docker.repostory>registry.cn-hangzhou.aliyuncs.com/bohai_repo</docker.repostory>
    </properties>
```

### add plugin

```xml
            <plugin>
                <groupId>com.spotify</groupId>
                <artifactId>dockerfile-maven-plugin</artifactId>
                <version>1.4.10</version>
                <executions>
                    <execution>
                        <id>default</id>
                        <goals>
                            <goal>build</goal>
                            <goal>push</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <repository>${docker.repostory}/${project.artifactId}</repository>
                    <tag>${project.version}</tag>
                    <buildArgs>
                        <JAR_FILE>target/${project.build.finalName}.jar</JAR_FILE>
                    </buildArgs>
                </configuration>
            </plugin>
```

### add dockerfile

```dockerfile
FROM openjdk:8u191-jre-alpine3.9
ENTRYPOINT ["/usr/bin/java", "-jar", "/app.jar"]
ARG JAR_FILE
ADD ${JAR_FILE} /app.jar
EXPOSE 8080
```

### Build

```shell
$ mvn install dockerfile:build

// list images
$ docker images registry.cn-hangzhou.aliyuncs.com/bohai_repo/demo:1.0.0-SNAPSHOT

// start images
$ docker run -it --rm -p 8080:8080 registry.cn-hangzhou.aliyuncs.com/bohai_repo/demo:1.0.0-SNAPSHOT

$ curl 127.0.0.1:8080
Hello World.This is an example of a spring docker
```

### Build and Push

```shell
$ mvn install dockerfile:push

[INFO] --- dockerfile:1.4.10:push (default-cli) @ demo ---
[INFO] The push refers to repository [registry.cn-hangzhou.aliyuncs.com/bohai_repo/demo]
[INFO] Image 72a6d2e10f94: Preparing
[INFO] Image 4222fe8d2ce7: Preparing
[INFO] Image 9afc0e59c268: Preparing
[INFO] Image b83703e07573: Preparing
[INFO] Image 4222fe8d2ce7: Layer already exists
[INFO] Image 9afc0e59c268: Layer already exists
[INFO] Image b83703e07573: Layer already exists
[INFO] Image 72a6d2e10f94: Pushing
[INFO] Image 72a6d2e10f94: Pushed
[INFO] 1.1.0-SNAPSHOT: digest: sha256:091d1dba02edc1754040c34d716001c5c4f30e5059fe387df6572cc1722117d1 size: 1159
```

### Apple Silicon M1 CPU

```shell
$ brew install socat
$ nohup socat TCP-LISTEN:2375,range=127.0.0.1/32,reuseaddr,fork UNIX-CLIENT:/var/run/docker.sock &> /dev/null &
$ export DOCKER_HOST=tcp://127.0.0.1:2375
```

## use apollo

### deploy apollo for docker

#### 一、create_db

```shell
CREATE USER 'apollo'@'%' identified BY 'xxxxxxxxxx';
CREATE DATABASE IF NOT EXISTS ApolloConfigDB DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
CREATE DATABASE IF NOT EXISTS ApolloPortalDB DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON ApolloConfigDB.* TO apollo@"%"; 
GRANT ALL PRIVILEGES ON ApolloPortalDB.* TO apollo@"%"; 
```

#### 二、init_db

```shell
# import sql file
apolloconfigdb: https://github.com/apolloconfig/apollo/blob/master/scripts/sql/apolloconfigdb.sql
apolloportaldb: https://github.com/apolloconfig/apollo/blob/master/scripts/sql/apolloportaldb.sql
```

#### 三、deploy 
```shell
# --net=host 
version=latest
docker pull apolloconfig/apollo-configservice:${version}
docker rm -f apollo-configservice
docker run --net=host \
    -e SPRING_DATASOURCE_URL="jdbc:mysql://xxxx:3306/ApolloConfigDB?characterEncoding=utf8" \
    -e SPRING_DATASOURCE_USERNAME=apollo -e SPRING_DATASOURCE_PASSWORD=xxxx \
    -d -v /data/apollo/configservice/logs:/opt/logs --name apollo-configservice apolloconfig/apollo-configservice:${version}

version=latest
docker pull apolloconfig/apollo-adminservice:${version}
docker rm -f apollo-adminservice
docker run -p 8090:8090 \
    -e SPRING_DATASOURCE_URL="jdbc:mysql://xxxx:3306/ApolloConfigDB?characterEncoding=utf8" \
    -e SPRING_DATASOURCE_USERNAME=apollo -e SPRING_DATASOURCE_PASSWORD=xxxx \
    -d -v /data/apollo/adminservice/logs:/opt/logs --name apollo-adminservice apolloconfig/apollo-adminservice:${version}

version=latest
docker pull apolloconfig/apollo-portal:${version}
docker rm -f apollo-portal
docker run -p 8070:8070 \
    -e SPRING_DATASOURCE_URL="jdbc:mysql://xxxx:3306/ApolloPortalDB?characterEncoding=utf8" \
    -e SPRING_DATASOURCE_USERNAME=apollo -e SPRING_DATASOURCE_PASSWORD=xxxxx \
    -e APOLLO_PORTAL_ENVS=dev \
    -e DEV_META=http://192.168.60.229:8080 \
    -e SPRING.PROFILES.ACTIVE=github \
    -d -v /data/apollo/portal/logs:/opt/logs --name apollo-portal apolloconfig/apollo-portal:${version}
```

### add dependency with pom

```xml
        <dependency>
            <groupId>com.ctrip.framework.apollo</groupId>
            <artifactId>apollo-client</artifactId>
            <version>1.1.0</version>
        </dependency>
```

### add new class: ApolloApplocation

```shell
package com.bohai.helloworld;

import com.ctrip.framework.apollo.ConfigService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApolloApplocation {
    @RestController
    @RequestMapping(path = "/config/")
    public class ApolloConfigurationController {

        @RequestMapping(path = "/{key}")
        public String getConfigForKey(@PathVariable("key") String key){
            return ConfigService.getAppConfig().getProperty( key, "undefined");
        }
    }
}
```

### add properties config file

file path: src/main/resources/application.properties

```shell
app.id=9025.uni-all-server.uni.ytzh
# apollo eureka url
apollo.meta=http://192.168.60.229:8080

apollo.bootstrap.enabled = true
apollo.bootstrap.eagerLoad.enabled=false
```

### requests

```shell
curl http://127.0.0.1/config/{name}
```

## use actuator

### add dependency with pom

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```

### add config

src/main/resources/application.yml

```yaml
management:
  server:
    port: 8443
  endpoints:
    web:
      exposure:
        include: "*"
      base-path: /actuator
  endpoint:
    shutdown:
      enabled: true
    health:
      show-details: always
```

### request

```shell
curl http://127.0.0.1:8443/actuator/
```

## use eureka

TODO

## use kubernetes serviceaccont

TODO