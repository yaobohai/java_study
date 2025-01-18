#!/bin/sh
java $JAVA_JAR -jar -Xmx${Xmx} -Xms${Xms} -Xmn${Xmn} -Xss1024k -XX:LargePageSizeInBytes=${Xml} \
             /opt/app/app.jar