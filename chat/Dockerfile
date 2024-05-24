LABEL authors="denis"
FROM maven:22-jdk as build
WORKDIR /app
COPY . .
RUN mvn clean install

FROM openjdk:22-jdk
WORKDIR /app
COPY --from=build /app/server/target/server-1.0-jar-with-dependencies.jar .
COPY --from=build /app/server_config.properties
CMD ["java", "-jar", "--enable-preview", "target/server-1.0-jar-with-dependencies.jar.jar"]