# Tahap 1: Build Aplikasi
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Tahap 2: Jalankan Aplikasi
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Port yang dibuka
EXPOSE 8080

# Perintah menjalankan aplikasi
ENTRYPOINT ["java", "-jar", "app.jar"]