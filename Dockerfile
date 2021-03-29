FROM maven:3.6.3-jdk-11-slim as builder
ARG DIR=/workspace/app
COPY src ${DIR}/src
COPY pom.xml ${DIR}
WORKDIR ${DIR}
RUN mvn clean package -DskipTests

FROM openjdk:11.0.8-jre-slim
ARG DIR=/workspace/app
COPY --from=builder ${DIR}/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
