# Sping demo

## add properties

```xml
    <properties>
        <java.version>1.8</java.version>
        <docker.repostory>registry.cn-hangzhou.aliyuncs.com/bohai_repo</docker.repostory>
    </properties>
```

## add plugin

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

## Build

```shell
$ mvn install dockerfile:build

// list images
$ docker images registry.cn-hangzhou.aliyuncs.com/bohai_repo/demo:1.0.0-SNAPSHOT

// start images
$ docker run -it --rm -p 8080:8080 registry.cn-hangzhou.aliyuncs.com/bohai_repo/demo:1.0.0-SNAPSHOT

$ curl 127.0.0.1:8080
Hello World.This is an example of a spring docker
```

## Apple Silicon M1 CPU

```shell
$ brew install socat
$ nohup socat TCP-LISTEN:2375,range=127.0.0.1/32,reuseaddr,fork UNIX-CLIENT:/var/run/docker.sock &> /dev/null &
$ export DOCKER_HOST=tcp://127.0.0.1:2375
```
