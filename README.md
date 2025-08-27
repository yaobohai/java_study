## java springboot devops

### 目录结构

```shell
. 
# 编译工程所使用的pom文件
├── pom.xml
└── src
    └── main
        # 存储dockerfile以及启动jar的脚本
        ├── docker
        │   ├── Dockerfile
        │   └── docker-entrypoint.sh
        # java源代码
        ├── java
        │   └── com
        │       └── bohai
        │           └── demo
        │               └── service
        │                   └── main.java
        # 程序启动配置文件
        └── resources
            └── application.properties
            
```

## 集成devops

### 监控(Actuator)

参考：https://init.ac/2025/04/27/springboot-actuator/

### 构建docker镜像

参考：https://init.ac/2025/04/30/docker-maven-plugin/

## 编译使用

仅编译jar包

```shell
# 编译后，jar制品位置位于：`target/` 下
mvn -U clean install -D maven.test.skip=true -D maven.javadoc.skip=true -am package
```


编译jar包同时触发编译docker制品(不推送镜像)

```shell
mvn -U clean install -D maven.test.skip=true -D maven.javadoc.skip=true -am -P docker
```

编译jar包同时触发编译docker制品(推送镜像)

```shell
mvn -U clean install -D maven.test.skip=true -D maven.javadoc.skip=true -am -P docker deploy
```
### 启动制品

```shell
# jar包启动
java -jar target/demo-service-1.0.0-SNAPSHOT.jar

# docker制品启动
docker run -itd demo-service \
-e Xmx=1024m \
-e Xms=512m \
-e Xmn=256m \
-e Xml=128m \
-p 8080:8080 \
-p 8443:8443 \
registry.cn-hangzhou.aliyuncs.com/bohai_repo/demo-service:1.0.0-SNAPSHOT
```

### 访问

访问主程序

```shell
curl 127.0.0.1:8080
```

访问程序监控接口

```shell
curl http://127.0.0.1:8443/actuator/metrics
```

访问程序健康检查接口

```shell
curl http://127.0.0.1:8443/actuator/health
```


访问程序下线接口(执行后，程序将退出运行)

```shell
curl -X POST http://127.0.0.1:8443/actuator/shutdown
```