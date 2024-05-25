FROM maven:3.9.6-eclipse-temurin-22-alpine as build
#FROM maven:3.8.6-openjdk-17 AS build
ARG MAVEN_OPTS
WORKDIR /app
COPY . .
RUN mvn clean install
#
FROM openjdk:22-jdk
WORKDIR /app
COPY --from=build /app/server/target/server-1.0-jar-with-dependencies.jar .
COPY --from=build /app/server_config.properties .
CMD ["java", "-jar", "--enable-preview", "server-1.0-jar-with-dependencies.jar", "-c" ,"server_config.properties"]
