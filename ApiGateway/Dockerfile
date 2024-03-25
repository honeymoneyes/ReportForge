FROM gradle:8.5 as builder
WORKDIR /app

COPY gradle gradle
COPY build.gradle settings.gradle gradlew ./
COPY src src

RUN gradle --no-daemon dependencies && \
    gradle --no-daemon bootJar

FROM bellsoft/liberica-openjre-alpine-musl:17
WORKDIR /app

ARG DEPENDENCY=/app/build/libs
COPY --from=builder ${DEPENDENCY}/*.jar app.jar

CMD ["java", "-jar", "app.jar"]