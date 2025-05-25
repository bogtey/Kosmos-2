FROM gradle:8.2.1-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle clean bootJar -x test --no-daemon

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]