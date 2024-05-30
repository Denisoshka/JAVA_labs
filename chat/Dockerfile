FROM maven:3.9.6-eclipse-temurin-22-alpine as build
#FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean install
#
FROM openjdk:22-jdk
WORKDIR /app
COPY --from=build /app/server/target/server-1.0-jar-with-dependencies.jar .
COPY --from=build /app/server_config.properties .
ENV PORT=127.0.0.1
ENV HOST=9876
CMD ["java", "-jar", "--enable-preview", "server-1.0-jar-with-dependencies.jar", "-p" ,"9876", "-h", "127.0.0.1"]
