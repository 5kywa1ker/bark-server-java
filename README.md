# bark-server-java

>该项目是[bark-server](https://github.com/Finb/bark-server)的java实现，基于SpringBoot,[pushy](https://github.com/jchambers/pushy)
## Docker 部署
``` sh
docker pull skywa1ker/bark-server-java:latest
```
``` sh
docker run -p 8081:8081 --name bark-java -v /data/bark/config:/data/bark/config -v /data/bark/log:/data/bark/log -v /data/bark/h2db:/data/bark/h2db --restart=always -d skywa1ker/bark-server-java:latest
```
