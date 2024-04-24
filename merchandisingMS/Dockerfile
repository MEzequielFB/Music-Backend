FROM maven:3-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
ARG PORT
ENV PORT=${PORT}
COPY --from=build /target/merchandisingMS-0.0.1-SNAPSHOT.jar merchandisingMS.jar

ENTRYPOINT [ "java", "-Dserver.port=${PORT}", "-jar","merchandisingMS.jar" ]