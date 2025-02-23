# Task Management System

## Описание
Task Management System — это API для управления задачами, разработанное на Java 21 с использованием Spring Boot, 
Spring Security и JWT для аутентификации. В качестве базы данных используется PostgreSQL.

## Функциональность
- Регистрация и аутентификация пользователей
- Создание, редактирование, удаление и просмотр задач
- Управление статусами и приоритетами задач
- Назначение исполнителей
- Фильтрация и пагинация задач
- JWT-аутентификация и ролевая модель (администратор, пользователь)
- Добавление, редактирование и удаление комментариев к задачам
- Разграничение прав пользователей (администратор может управлять всеми задачами, пользователь — только своими)
- Обработчик ошибок для возврата понятных сообщений
- Кэширование данных для оптимизации запросов

## Технологии
- Java 21
- Spring Boot 3
- Spring Security + JWT
- PostgreSQL
- Docker, Docker Compose
- Swagger (OpenAPI) для документации

## Запуск проекта

### Требования
- Docker и Docker Compose
- JDK 17+
- Maven

### Шаги для запуска
1. Соберите проект:
   ```sh
   ./gradlew build
   ```
2. Запустите контейнеры:
   ```sh
   docker-compose up -d
   ```
3. API будет доступно по адресу:

   http://localhost:8080

4. API Документация:

   http://localhost:8080/swagger-ui.html

## API Endpoints
- `POST /api/auth/login` - Authenticate and get JWT
- `POST /api/tasks` - Create a new task
- `GET /api/tasks` - Retrieve all tasks
- `PUT /api/tasks/{id}` - Update a task
- `DELETE /api/tasks/{id}` - Delete a task
- `POST /api/tasks/{taskId}/comments` - Add a comment to a task
- `GET /api/tasks/{taskId}/comments` - Retrieve all comments for a task

## Структура проекта

```
task-management-system/
│── src/
│   ├── main/
│   │   ├── java/com/example/taskmanagement/
│   │   │   ├── TaskManagementApplication.java
│   │   │   │
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   ├── JwtUtil.java
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── TaskController.java
│   │   │   │   ├── CommentController.java
│   │   │   │
│   │   │   ├── dto/
│   │   │   │   ├── AuthRequest.java
│   │   │   │   ├── AuthResponse.java
│   │   │   │   ├── TaskDto.java
│   │   │   │   ├── CommentDto.java
│   │   │   │
│   │   │   ├── model/
│   │   │   │   ├── User.java
│   │   │   │   ├── Task.java
│   │   │   │   ├── Comment.java
│   │   │   │   ├── Role.java
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── TaskRepository.java
│   │   │   │   ├── CommentRepository.java
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── TaskService.java
│   │   │   │   ├── CommentService.java
│   │   │   │
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │
│   │   │   ├── util/
│   │   │   │   ├── MapperUtil.java
│   │   │   │
│   │   │   ├── security/
│   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   ├── CustomUserDetailsService.java
│   │   │   │
│   ├── resources/
│   │   ├── application.yml
│   │   ├── data.sql
│   │   ├── schema.sql
│
│── test/
│   ├── java/com/example/taskmanagement/
│   │   ├── TaskManagementApplicationTests.java
│   │   ├── AuthServiceTests.java
│   │   ├── TaskServiceTests.java
│   │   ├── CommentServiceTests.java
│
│── docker-compose.yml
│── Dockerfile
│── pom.xml
│── README.md
```


