# Этап 1 - Сборка приложения

FROM gradle:8.13-jdk21 AS builder

WORKDIR /app

COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradlew .

COPY src src

RUN ./gradlew clean bootJar --no-daemon

# Этап 2 - Создание итогового образа для запуска

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]