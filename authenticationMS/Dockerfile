FROM maven:3-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/authenticationMS-0.0.1-SNAPSHOT.jar authenticationMS.jar
EXPOSE 8084
ENTRYPOINT [ "java","-jar","authenticationMS.jar" ]