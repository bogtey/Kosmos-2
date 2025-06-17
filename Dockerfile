# Используем официальный образ Gradle для сборки артефакта
FROM gradle:8.7-jdk17 AS build
WORKDIR /app

COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
COPY . .

RUN chmod +x gradlew

RUN ./gradlew bootJar --no-daemon --warning-mode all

# Этап запуска
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/build/libs/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]