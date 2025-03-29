# Dockerfile (멀티 스테이지; gradlew 대신 gradle 명령어 사용)
FROM gradle:8.2-jdk17 AS builder
WORKDIR /home/gradle/project

# 소스코드 복사 (Jenkins가 git clone해서 작업 디렉토리에 있다고 가정)
COPY . .

# gradlew 대신 시스템 gradle을 사용하여 bootJar 생성 (테스트 제외)
RUN gradle clean bootJar -x test --no-daemon

# -------------------------------------
FROM openjdk:17-jdk-slim
WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일을 복사
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
