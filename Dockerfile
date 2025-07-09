FROM openjdk:17-jdk-alpine3.14 AS builder
EXPOSE 8080

# Add a volume pointing to /tmp
VOLUME /tmp
WORKDIR /app
COPY . /app/

RUN  ./mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine3.14

LABEL authors="sabrinasergio"

WORKDIR /app

COPY target/wallet-0.0.1-SNAPSHOT.jar /app/wallet.jar

ENV SPRING_PROFILES_ACTIVE=docker


ENTRYPOINT ["java", "-jar", "wallet.jar"]