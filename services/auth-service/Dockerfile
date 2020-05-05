FROM openjdk:11-slim as builder
WORKDIR /usr/local/app
COPY .mvn .mvn
COPY src src
COPY mvnw mvnw
COPY pom.xml pom.xml
RUN ./mvnw -Dmaven.test.skip=true package

FROM openjdk:11-slim
ARG JAR_FILE=/usr/local/app/target/*.jar
COPY --from=builder ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]