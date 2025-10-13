FROM eclipse-temurin:21-jdk

WORKDIR /backend

COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradlew .

COPY src src

RUN ./gradlew --no-daemon build -x test

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=60.0 -XX:InitialRAMPercentage=50.0"
EXPOSE 8080

CMD java -jar build/libs/app-0.0.1-SNAPSHOT.jar