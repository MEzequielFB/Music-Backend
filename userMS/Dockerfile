FROM maven:3-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/userMS-0.0.1-SNAPSHOT.jar userMS.jar
EXPOSE 8081
ENTRYPOINT [ "java","-jar","userMS.jar" ]