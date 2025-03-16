# 使用稳定的 OpenJDK 17 JRE 作为基础镜像
FROM openjdk:25-jdk AS runtime

# 设置工作目录
WORKDIR /app

# 将构建的 JAR 文件复制到容器中
COPY target/spring-boot-docker-1.0.jar app.jar

# 暴露应用程序的端口（根据你的应用修改端口号）
EXPOSE 8080

# 支持通过环境变量动态设置端口和 JVM 参数
ARG PORT=8080
ARG JVM_OPTS="-Xms128m -Xmx512m"
ENV APP_PORT=${PORT}

# 设置健康检查
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s \
  CMD curl --fail http://localhost:${APP_PORT}/actuator/health || exit 1

# 设置启动命令，包含 JVM 参数和日志配置
ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} -jar app.jar"]
