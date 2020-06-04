FROM openjdk:8-jdk

ARG JAR_FILE=bark-server-java.jar
USER root
# 设置时区 安装ps命令
ENV TZ=Asia/Shanghai
RUN set -eux; \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime; \
    echo $TZ > /etc/timezone; \
    apt-get update && apt-get install -y procps && apt-get install -y maven
# 新建应用目录
ARG HOME=/data/bark
RUN mkdir -p $HOME/config;mkdir $HOME/log;mkdir $HOME/h2db;mkdir $HOME/code;ls -la $HOME
# build jar
ADD ./ $HOME/code/
WORKDIR $HOME/code
RUN set -eux;ls -la;mvn clean package -DskipTests && cp $HOME/code/target/$JAR_FILE $HOME

# 启动脚本
WORKDIR $HOME
ENTRYPOINT ["java", "-jar", "$JAR_FILE"]

# 端口
EXPOSE 8081