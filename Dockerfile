# Используем официальный образ Maven для сборки артефакта
FROM maven:3.8.1-openjdk-11 AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем pom.xml и скачиваем зависимости
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Копируем остальной код проекта и собираем артефакт
COPY src ./src
RUN mvn package -DskipTests

# Используем официальный образ OpenJDK для запуска приложения
FROM openjdk:11-jre-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный артефакт из предыдущего этапа
COPY --from=build /app/target/*.jar app.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]

# Экспонируем порт
EXPOSE 8080
