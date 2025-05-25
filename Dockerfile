FROM gradle:8.7-jdk17 AS build
WORKDIR /app

COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
COPY . .

RUN ./gradlew bootJar --no-daemon

# Этап запуска
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/build/libs/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]