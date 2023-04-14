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

TODO

## use eureka

TODO

## use kubernetes serviceaccont

TODO