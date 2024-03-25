FROM gradle:8.5 as builder
WORKDIR /app

COPY gradle gradle
COPY src src
COPY build.gradle settings.gradle gradlew ./

RUN gradle --no-daemon dependencies && \
    gradle --no-daemon bootJar

FROM bellsoft/liberica-openjre-alpine-musl:17
WORKDIR /app

ARG DEPENDENCY=/app/build/libs
COPY --from=builder ${DEPENDENCY}/*.jar app.jar

CMD ["java", "-jar", "app.jar"]