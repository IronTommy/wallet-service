# Используем официальный образ для Java
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем все файлы проекта в контейнер
COPY . /app

# Собираем проект и запускаем приложение
RUN ./mvnw clean package -DskipTests

# Запускаем Spring Boot приложение
ENTRYPOINT ["java", "-jar", "target/wallet-0.0.1-SNAPSHOT.jar"]
