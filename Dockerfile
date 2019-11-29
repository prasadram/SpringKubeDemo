FROM openjdk:8

WORKDIR /app

ARG JAR_FILE=target/springkubedemo-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} springkubedemo.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/springkubedemo.jar"]