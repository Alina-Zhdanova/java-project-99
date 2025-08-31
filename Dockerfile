FROM gradle:8.13-jdk21

WORKDIR /app

COPY . .

RUN ["./gradlew", "clean", "build"]

CMD ["./gradlew", "run"]