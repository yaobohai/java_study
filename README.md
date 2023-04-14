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
