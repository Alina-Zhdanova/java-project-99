FROM gradle:8.13-jdk21

WORKDIR /app

COPY . .

RUN ["./gradlew", "clean", "build"]

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=60.0 -XX:InitialRAMPercentage=50.0"
EXPOSE 8080

CMD ["./gradlew", "run"]