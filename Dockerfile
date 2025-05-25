FROM gradle:8.2.1-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test --no-daemon

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]