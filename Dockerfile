FROM gradle:8.7-jdk17 AS build
WORKDIR /app

# Копируем Gradle wrapper и конфигурации
COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

# Копируем всё остальное
COPY . .

# Собираем jar
RUN ./gradlew bootJar --no-daemon

# Этап запуска
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/build/libs/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]