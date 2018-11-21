FROM maven:3.6.0-jdk-11 as builder

ADD . /var/source

WORKDIR /var/source

RUN mvn package -DskipTests

FROM openjdk:11

WORKDIR /opt/api

COPY --from=builder /var/source/target/api.jar /opt/api/api.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "api.jar"]

