
# Kata3 Backend

## Overview

Kata3 is a Spring Boot REST API for managing users and items (projects or tasks) with JWT-based authentication. The backend uses MongoDB as the database and is designed for deployment on Heroku. It provides endpoints for user registration, login, and item CRUD operations, with all responses formatted in JSON. The project is built with Java 21 and Spring Boot 3.5.0, using Gradle (Kotlin DSL) for dependency management.

---

## Metadata

- **Project Name:** Kata3
- **Version:** 0.0.1-SNAPSHOT
- **Java Version:** 21
- **Spring Boot Version:** 3.5.0
- **Build Tool:** Gradle (Kotlin DSL)
- **Database:** MongoDB (MongoDB Atlas recommended)
- **Authentication:** JWT (JSON Web Tokens)
- **Deployment:** Heroku
- **License:** MIT (assumed, adjust as needed)

---

## Prerequisites

- **Java 21:** Install a JDK (e.g., OpenJDK or Oracle JDK).
- **MongoDB:** A MongoDB instance (local or MongoDB Atlas).  
  Set the `SPRING_DATA_MONGODB_URI` environment variable (e.g.,
  ```  
  mongodb+srv://<username>:<password>@cluster0.abcde5t.mongodb.net/kata3?retryWrites=true&w=majority&appName=Cluster0  
  ```
- **Gradle:** Gradle 8.x or later (included in the project via `gradlew`).
- **Heroku CLI:** For deployment (optional).
- **Insomnia/Postman:** For testing endpoints.

---

## Project Folder Structure

```
kata3/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/kata3/kata3/
│   │   │       ├── controller/
│   │   │       │   ├── AuthController.java
│   │   │       │   ├── ItemController.java
│   │   │       │   └── UserController.java
│   │   │       ├── data/
│   │   │       │   ├── dto/
│   │   │       │   │   ├── ItemDto.java
│   │   │       │   │   ├── TokenDto.java
│   │   │       │   │   └── UserDto.java
│   │   │       │   ├── entity/
│   │   │       │   │   ├── Item.java
│   │   │       │   │   └── User.java
│   │   │       │   └── repository/
│   │   │       │       ├── ItemRepository.java
│   │   │       │       └── UserRepository.java
│   │   │       ├── service/
│   │   │       │   ├── ItemService.java
│   │   │       │   └── UserService.java
│   │   │       ├── util/
│   │   │       │   └── JwtUtil.java
│   │   │       ├── config/
│   │   │       │   └── SecurityConfig.java
│   │   │       └── Application.java
│   │   └── resources/
│   │       └── application.properties
├── .env
├── .gitignore
├── build.gradle.kts
├── gradlew
├── gradlew.bat
└── README.md
```

---

## Setup Instructions

### Local Development

1. **Clone the Repository**:
```bash
git clone <repository-url>
cd kata3
```

2. **Configure Environment Variables**:

Create a `.env` file in the project root (or set environment variables directly):
```bash
SPRING_DATA_MONGODB_URI=mongodb+srv://<username>:<password>@cluster0.abcde5t.mongodb.net/kata3?retryWrites=true&w=majority&appName=Cluster0
JWT_SECRET=your-very-secure-secret-key-here-at-least-32-bytes
```

Ensure `<password>` is URL-encoded (e.g., `@` as `%40`).  
The `.env` file is gitignored to prevent committing sensitive data.

3. **Run the Application**:
```bash
./gradlew bootRun
```

The application starts on [http://localhost:8080](http://localhost:8080).

4. **Test Endpoints**:

Use Insomnia or Postman. Import `Kata3_API_Insomnia_Collection.json`.

---

### Heroku Deployment

1. **Set Up Heroku**:
```bash
heroku login
heroku create <app-name>
```

2. **Set Environment Variables**:
```bash
heroku config:set SPRING_DATA_MONGODB_URI="mongodb+srv://<username>:<password>@cluster0.abcde5t.mongodb.net/kata3?retryWrites=true&w=majority&appName=Cluster0"
heroku config:set JWT_SECRET="your-very-secure-secret-key-here-at-least-32-bytes"
```

3. **Deploy**:
```bash
git push heroku main
```

Access the app at `https://<app-name>.herokuapp.com`.

---

## Dependencies

- `spring-boot-starter-web`
- `spring-boot-starter-security`
- `spring-boot-starter-data-mongodb`
- `lombok`
- `jjwt` (0.12.6)
- `java-dotenv` (5.2.2)

See `build.gradle.kts` for the full list.

---

## Endpoints Overview

All endpoints return JSON. Base URL: `http://localhost:8080` or `https://<app-name>.herokuapp.com`.

### AuthController

#### POST `/auth/register`

**Request**:
```json
{
  "username": "testuser",
  "password": "password123"
}
```

**Response**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### POST `/auth/login`

**Request**:
```json
{
  "username": "testuser",
  "password": "password123"
}
```

**Response**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

### UserController

#### DELETE `/auth/reset`

**Header**:
```
Authorization: Bearer <jwt_token>
```

**Response**:
```json
{
  "success": true
}
```

---

### ItemController

All endpoints require JWT.

#### GET `/items`

**Header**:
```
Authorization: Bearer <jwt_token>
```

**Response**:
```json
[
  {
    "id": "68476f0ac2c460d0773b8c63",
    "type": "PROJECT",
    "title": "My Project",
    "description": "A test project",
    "finished": false,
    "userId": "68476a31c2c460d0773b8c61"
  }
]
```

#### GET `/items/{id}`

#### POST `/items`

**Request**:
```json
{
  "type": "PROJECT",
  "title": "My Project",
  "description": "A test project",
  "finished": false
}
```

**Response**:
```json
{
  "id": "68476f0ac2c460d0773b8c63",
  "type": "PROJECT",
  "title": "My Project",
  "description": "A test project",
  "finished": false,
  "userId": "68476a31c2c460d0773b8c61"
}
```

#### PATCH `/items/{id}`

**Request**:
```json
{
  "title": "Updated Project",
  "finished": true
}
```

#### DELETE `/items/{id}`

#### DELETE `/items/reset`

---

## Security

- JWT tokens required in the `Authorization` header.
- Tokens expire after 10 hours.
- Ensure environment variables are secure.
- MongoDB user must have readWrite access.

---

## Testing

Use Insomnia + collection file.

**Steps**:
1. Register via `/auth/register`
2. Copy token to Insomnia env
3. Test `/items` endpoints
4. Use reset endpoints for cleanup

---

## Troubleshooting

- `401 Unauthorized`: Re-authenticate.
- MongoDB issues: check URI & whitelist.
- Check logs for stack traces.

---

## Notes

- **Reset endpoints** are for development.
- Protect them in production.
- MongoDB creates database on first write.

**Future Improvements**:
- Role-based access for admin actions.
- Add unit tests with JUnit/Mockito.
- Better JSON error messages.

---

## Contributing

```bash
# Example workflow
git checkout -b feature/xyz
git commit -m "Add feature xyz"
git push origin feature/xyz
```

Then open a PR.

---

## Contact

For support, contact: `[your-email@example.com]` or open an issue.
