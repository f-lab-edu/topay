FROM gradle:8.2-jdk17 AS builder
WORKDIR /home/gradle/project
COPY . .
RUN ./gradlew clean bootJar -x test --no-daemon

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
