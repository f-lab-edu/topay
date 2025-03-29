# Dockerfile (단일 스테이지: JAR만 복사)
FROM openjdk:17-jdk-slim
WORKDIR /app

# Jenkins에서 빌드된 JAR을 이 디렉토리로 COPY
COPY *.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
