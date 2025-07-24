# 1. Aşama: Build aşaması
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. Aşama: Sadece jar dosyasını kullan, daha küçük image
FROM openjdk:17-jdk-alpine
WORKDIR /app

ENTRYPOINT ["java", "-jar", "app.jar"]
