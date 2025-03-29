# Dockerfile (멀티 스테이지)
FROM gradle:8.2-jdk17 AS builder
WORKDIR /home/gradle/project

# 소스코드 복사 (Jenkins가 git clone 해서 작업 디렉토리 내에 있다고 가정)
COPY . .

# gradlew에 실행 권한 부여 후, gradlew 실행하여 bootJar 생성 (테스트 제외)
RUN chmod +x gradlew && ./gradlew clean bootJar -x test --no-daemon

# -------------------------------------
FROM openjdk:17-jdk-slim
WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일을 복사
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
