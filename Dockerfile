# syntax=docker/dockerfile:1.7

FROM maven:3.9.11-eclipse-temurin-25 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -q -DskipTests package

FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=build /app/target/todo_voodoo-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-cp", "app.jar", "org.todo_voodoo.Main"]
