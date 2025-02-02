# 使用官方 OpenJDK 1.8 作为基础镜像
FROM openjdk:8-jdk-alpine

# 设置工作目录
WORKDIR /app

# 将构建的 JAR 文件复制到容器中
COPY target/tiny-hacknews.jar app.jar

# 暴露后端服务端口（假设为8080）
EXPOSE 8080

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]