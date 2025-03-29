# Dockerfile (멀티 스테이지)
FROM gradle:8.2-jdk17 AS builder
WORKDIR /home/gradle/project

# 소스코드 복사 (Jenkins가 git clone 해서 작업 디렉토리 내에 있다고 가정)
COPY . .

# gradlew 실행
RUN ./gradlew clean bootJar -x test --no-daemon

# -------------------------------------
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
