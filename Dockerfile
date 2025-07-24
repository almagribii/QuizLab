# D:\IdeaProjec\QuizLab\Dockerfile
# Pastikan file ini HANYA berisi instruksi Docker ini.

# -- Tahap 1: Build Aplikasi Spring Boot --
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

COPY src src

RUN chmod +x gradlew

RUN ./gradlew clean build -x test

# -- Tahap 2: Jalankan Aplikasi (Image Final yang Lebih Kecil) --
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=builder /app/build/libs/QuizLab-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]