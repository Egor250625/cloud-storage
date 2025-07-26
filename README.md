# ☁️ Cloud Storage Service | Облачное хранилище файлов

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?logo=openjdk&style=flat)
![Spring](https://img.shields.io/badge/Spring_Boot-3.0-6DB33F?logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-316192?logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-24.0-2496ED?logo=docker)

**Многопользовательское файловое хранилище** с веб-интерфейсом и REST API. Полный аналог Google Drive с возможностью безопасного хранения и управления файлами.

## 🌟 Ключевые особенности

- 🔐 **Безопасное хранение** файлов с изоляцией данных пользователей
- 📁 **Полный набор операций**: загрузка, скачивание, переименование, перемещение
- 🔍 **Умный поиск** по всем вашим файлам
- 📂 **Поддержка папок** с древовидной структурой
- ⚡ **Высокая производительность** благодаря Spring Boot и Redis
- 🐳 **Простое развертывание** через Docker Compose

## 🚀 Быстрый старт

### Требования
- Docker 24.0+
- Docker Compose 2.20+

## 🛠️ Технологии проекта

### 🔙 **Бэкенд**
<p align="left">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white" alt="Spring Security">
  <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle">
</p>

- Java 17+
- Spring Boot (основа приложения)
- Spring Web MVC (REST API)
- Spring Security (аутентификация)
- Spring Sessions (управление сессиями)
- Lombok (уменьшение boilerplate кода)
- MapStruct (преобразование DTO)
- Swagger / OpenAPI 3 (документация API)

### 🗄️ **Базы данных и хранилища**
<p align="left">
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white" alt="Redis">
  <img src="https://img.shields.io/badge/MinIO-FF0000?style=for-the-badge&logo=minio&logoColor=white" alt="MinIO">
  <img src="https://img.shields.io/badge/Liquibase-2962FF?style=for-the-badge&logo=liquibase&logoColor=white" alt="Liquibase">
</p>

- PostgreSQL (основная база данных)
- Redis (хранение сессий пользователей)
- MinIO (S3-совместимое файловое хранилище)
- Spring Data JPA (работа с базой данных)
- Liquibase (миграции базы данных)

### 🧪 **Тестирование**
<p align="left">
  <img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white" alt="JUnit5">
  <img src="https://img.shields.io/badge/Testcontainers-3766AB?style=for-the-badge&logo=docker&logoColor=white" alt="Testcontainers">
</p>

- JUnit 5 (модульное тестирование)
- Testcontainers (интеграционные тесты)
- Spring Test (тестирование Spring компонентов)

## ⚙️ Настройки по умолчанию

| Сервис       | URL/Порт             | Учетные данные              |
|--------------|----------------------|-----------------------------|
| **MinIO**    | http://localhost:9001| Логин: `minioadmin`         |
|              |                      | Пароль: `minioadmin`        |
| **PostgreSQL**| `localhost:5432`    | Пользователь: `postgres`    |
|              |                      | Пароль: `postgres`          |
| **Redis**    | `localhost:6379`     | Без аутентификации          |

## 📚 API документация

API следует REST-архитектуре и доступно по базовому пути `/api`. Все запросы (кроме авторизации) требуют аутентификации через сессионные куки.

### 🔐 Аутентификация

| Метод | Эндпоинт               | Описание                     |
|-------|------------------------|-----------------------------|
| POST  | `/api/auth/sign-up`    | Регистрация нового пользователя |
| POST  | `/api/auth/sign-in`    | Вход в систему               |
| POST  | `/api/auth/sign-out`   | Выход из системы             |

### 📂 Работа с файлами

| Метод   | Эндпоинт                          | Описание                     |
|---------|-----------------------------------|-----------------------------|
| GET     | `/api/resource?path={path}`       | Информация о файле/папке    |
| DELETE  | `/api/resource?path={path}`       | Удаление файла/папки        |
| GET     | `/api/resource/download?path={path}` | Скачивание файла/папки (zip)|
| GET     | `/api/resource/move?from={from}&to={to}` | Переименование/перемещение |
| GET     | `/api/resource/search?query={query}` | Поиск файлов              |
| POST    | `/api/resource?path={path}`       | Загрузка файлов             |

### 📁 Работа с папками

| Метод | Эндпоинт                   | Описание                  |
|-------|----------------------------|--------------------------|
| GET   | `/api/directory?path={path}` | Содержимое папки        |
| POST  | `/api/directory?path={path}` | Создание новой папки    |

🔹 Полная документация доступна через Swagger UI после запуска приложения:  
[http://31.56.48.72:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

# DEPLOY - http://31.56.48.72:3000/
