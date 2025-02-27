# Task Management System

Система управления задачами (Task Management System) — это простое веб-приложение, разработанное для создания, редактирования, удаления и просмотра задач. Система поддерживает аутентификацию и авторизацию пользователей, а также ролевую модель (администратор и пользователь).

## Функционал

### Основные возможности:
- **Аутентификация и авторизация**:
   - Регистрация и вход по email и паролю.
   - Использование JWT-токенов для доступа к API.
- **Ролевая модель**:
   - Администратор: может управлять всеми задачами (создавать, редактировать, удалять, назначать исполнителей, менять статус и приоритет).
   - Пользователь: может управлять только своими задачами (менять статус, оставлять комментарии).
- **Управление задачами**:
   - Создание, редактирование, удаление задач.
   - Назначение исполнителя задачи.
   - Изменение статуса задачи (например, "в ожидании", "в процессе", "завершено").
   - Установка приоритета задачи (например, "высокий", "средний", "низкий").
- **Комментарии**:
   - Добавление комментариев к задачам.
   - Удаление комментариев (только администратор или автор комментария).
- **Фильтрация и пагинация**:
   - Получение задач по автору или исполнителю.
   - Пагинация для списка задач и комментариев.

### Дополнительные возможности:
- Валидация входящих данных.
- Обработка ошибок с понятными сообщениями.
- Документация API с использованием Swagger.

## Примененные технологии

- **Язык программирования**: Java 21
- **Фреймворк**: Spring Boot, Spring Security, Spring Data JPA
- **База данных**: PostgreSQL
- **Аутентификация**: JWT (JSON Web Token)
- **Документация API**: Swagger (OpenAPI 3.0)
- **Миграции базы данных**: Flyway
- **Сборка и управление зависимостями**: Maven
- **Контейнеризация**: Docker, Docker Compose
- **Логирование**: SLF4J с Logback
- **Тестирование**: JUnit, Mockito (для модульных тестов)

## Локальный запуск проекта

### Предварительные требования

1. Установите [Docker](https://docs.docker.com/get-docker/) и [Docker Compose](https://docs.docker.com/compose/install/).
2. Установите [Java 17](https://openjdk.org/projects/jdk/17/).
3. Установите [Maven](https://maven.apache.org/install.html).

### Шаги для запуска

1. **Клонируйте репозиторий**:
   ```bash
   git clone https://github.com/your-repo/task-management-system.git
   cd task-management-system
   ```

2. **Настройте переменные окружения:**:
   ```env
   TM_DB=task_manager_db
   TM_USER=postgres  
   TM_PASSWORD=yourpassword  
   JWT_SECRET_KEY=your-jwt-secret-key  
   INIT_ADMIN_FIRSTNAME=Admin  
   INIT_ADMIN_LASTNAME=Admin  
   INIT_ADMIN_EMAIL=admin@example.com  
   INIT_ADMIN_PASSWORD=admin123
   LOG_PATH=logs
   LOG_FILE=application.log
   ```
3. **Запустите проект с помощью Docker Compose:**:
   ```bash
   docker-compose up --build
   ```
   Это запустит:
- PostgreSQL базу данных.
- Spring Boot приложение.

4. Доступ к приложению:
- Приложение будет доступно по адресу: http://localhost:8080.
- Swagger UI для тестирования API: http://localhost:8080/swagger-ui.html.

5. Остановка проекта:
   Чтобы остановить проект, выполните:
    ```bash
   docker-compose down
   ```

### Миграции базы данных
Миграции базы данных выполняются автоматически с помощью Flyway при запуске приложения.
Все миграции находятся в папке src/main/resources/db/migration.

## Документация API
API документировано с использованием Swagger. После запуска проекта откройте Swagger UI по адресу:  
http://localhost:8080/swagger-ui.html  
Здесь вы найдете описание всех доступных эндпоинтов, параметров запросов и примеры ответов.

## Примеры запросов

### Регистрация пользователя
   ```bash
curl -X POST "http://localhost:8080/api/v1/auth/register" \
-H "Content-Type: application/json" \
-d '{
  "firstname": "John",
  "lastname": "Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "role": "USER"
}'
   ```

### Аутентификация
   ```bash
 curl -X POST "http://localhost:8080/api/v1/auth/authenticate" \
-H "Content-Type: application/json" \
-d '{
  "email": "john.doe@example.com",
  "password": "password123"
}'
}'
   ```

### Создание задачи
   ```bash
 curl -X POST "http://localhost:8080/api/v1/tasks" \
-H "Content-Type: application/json" \
-H "Authorization: Bearer <JWT_TOKEN>" \
-d '{
  "title": "Новая задача",
  "description": "Описание задачи",
  "status": "PENDING",
  "priority": "HIGH"
}'
   ```

#### Автор: Андрей Крецев
#### GitHub: https://github.com/akrecev
#### Email: akrecev@gmail.com
#### @akrecev