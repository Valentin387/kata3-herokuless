Kata3 Backend
Overview
Kata3 is a Spring Boot REST API for managing users and items (projects or tasks) with JWT-based authentication. The backend uses MongoDB as the database and is designed for deployment on Heroku. It provides endpoints for user registration, login, and item CRUD operations, with all responses formatted in JSON. The project is built with Java 21 and Spring Boot 3.5.0, using Gradle (Kotlin DSL) for dependency management.
Metadata

Project Name: Kata3
Version: 0.0.1-SNAPSHOT
Java Version: 21
Spring Boot Version: 3.5.0
Build Tool: Gradle (Kotlin DSL)
Database: MongoDB (MongoDB Atlas recommended)
Authentication: JWT (JSON Web Tokens)
Deployment: Heroku
License: MIT (assumed, adjust as needed)

Prerequisites

Java 21: Install a JDK (e.g., OpenJDK or Oracle JDK).
MongoDB: A MongoDB instance (local or MongoDB Atlas). Set the SPRING_DATA_MONGODB_URI environment variable (e.g., mongodb+srv://<username>:<password>@cluster0.abcde5t.mongodb.net/kata3?retryWrites=true&w=majority&appName=Cluster0).
Gradle: Gradle 8.x or later (included in the project via gradlew).
Heroku CLI: For deployment (optional).
Insomnia/Postman: For testing endpoints.

Project Folder Structure
kata3/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/kata3/kata3/
│   │   │       ├── controller/          # REST controllers
│   │   │       │   ├── AuthController.java
│   │   │       │   ├── ItemController.java
│   │   │       │   └── UserController.java
│   │   │       ├── data/
│   │   │       │   ├── dto/            # Data Transfer Objects
│   │   │       │   │   ├── ItemDto.java
│   │   │       │   │   ├── TokenDto.java
│   │   │       │   │   └── UserDto.java
│   │   │       │   ├── entity/         # MongoDB entities
│   │   │       │   │   ├── Item.java
│   │   │       │   │   └── User.java
│   │   │       │   └── repository/     # MongoDB repositories
│   │   │       │       ├── ItemRepository.java
│   │   │       │       └── UserRepository.java
│   │   │       ├── service/            # Business logic
│   │   │       │   ├── ItemService.java
│   │   │       │   └── UserService.java
│   │   │       ├── util/              # Utility classes
│   │   │       │   └── JwtUtil.java
│   │   │       ├── config/            # Spring configuration
│   │   │       │   └── SecurityConfig.java
│   │   │       └── Application.java   # Main application class
│   │   └── resources/
│   │       └── application.properties # Configuration properties
├── .env                               # Environment variables (gitignored)
├── .gitignore                         # Git ignore file
├── build.gradle.kts                   # Gradle build configuration
├── gradlew                            # Gradle wrapper
├── gradlew.bat                        # Gradle wrapper for Windows
└── README.md                          # This file

Setup Instructions
Local Development

Clone the Repository:
git clone <repository-url>
cd kata3


Configure Environment Variables:

Create a .env file in the project root (or set environment variables directly):SPRING_DATA_MONGODB_URI=mongodb+srv://<username>:<password>@cluster0.abcde5t.mongodb.net/kata3?retryWrites=true&w=majority&appName=Cluster0
JWT_SECRET=your-very-secure-secret-key-here-at-least-32-bytes


Ensure <password> is URL-encoded (e.g., @ as %40).
The .env file is gitignored to prevent committing sensitive data.


Run the Application:
./gradlew bootRun


The application starts on http://localhost:8080 (Tomcat, context path /).


Test Endpoints:

Use Insomnia or Postman to test endpoints (see below).
Import the Insomnia collection (Kata3_API_Insomnia_Collection.json) for pre-configured requests.



Heroku Deployment

Set Up Heroku:

Install the Heroku CLI.
Log in: heroku login.
Create an app: heroku create <app-name>.


Set Environment Variables:
heroku config:set SPRING_DATA_MONGODB_URI="mongodb+srv://<username>:<password>@cluster0.abcde5t.mongodb.net/kata3?retryWrites=true&w=majority&appName=Cluster0"
heroku config:set JWT_SECRET="your-very-secure-secret-key-here-at-least-32-bytes"


Deploy:
git push heroku main


Verify:

Access the app at https://<app-name>.herokuapp.com.
Test endpoints using Insomnia/Postman.



Dependencies

Spring Boot Starter Web: REST API support.
Spring Boot Starter Security: JWT authentication.
Spring Boot Starter Data MongoDB: MongoDB integration.
Lombok: Boilerplate reduction.
JJWT (0.12.6): JWT generation and validation.
java-dotenv (5.2.2): Environment variable loading from .env.

See build.gradle.kts for the full list.
Endpoints Overview
All endpoints return JSON responses and are secured with JWT authentication where applicable. The base URL is http://localhost:8080 (local) or https://<app-name>.herokuapp.com (Heroku).
Authentication Endpoints (AuthController)

POST /auth/register

Description: Registers a new user and returns a JWT token.
Request Body:{
"username": "testuser",
"password": "password123"
}


Response (200 OK):{
"token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}


Errors: 400 Bad Request (username exists, invalid input).


POST /auth/login

Description: Authenticates a user and returns a JWT token.
Request Body:{
"username": "testuser",
"password": "password123"
}


Response (200 OK):{
"token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}


Errors: 400 Bad Request (invalid credentials).



User Management Endpoint (UserController)

DELETE /auth/reset
Description: Deletes all users from the users collection.
Headers: Authorization: Bearer <jwt_token>
Response (200 OK):{
"success": true
}


Errors: 401 Unauthorized (invalid/missing JWT).



Item Management Endpoints (ItemController)
All endpoints require JWT authentication.

GET /items

Description: Retrieves items for the authenticated user, optionally filtered by type (PROJECT or TASK).
Query Param: type (optional, e.g., ?type=PROJECT)
Headers: Authorization: Bearer <jwt_token>
Response (200 OK):[
{
"id": "68476f0ac2c460d0773b8c63",
"type": "PROJECT",
"title": "My Project",
"description": "A test project",
"finished": false,
"userId": "68476a31c2c460d0773b8c61"
}
]


Errors: 401 Unauthorized.


GET /items/{id}

Description: Retrieves an item by ID for the authenticated user.
Path Param: id (MongoDB ObjectId)
Headers: Authorization: Bearer <jwt_token>
Response (200 OK):{
"id": "68476f0ac2c460d0773b8c63",
"type": "PROJECT",
"title": "My Project",
"description": "A test project",
"finished": false,
"userId": "68476a31c2c460d0773b8c61"
}


Errors: 401 Unauthorized, 404 Not Found.


POST /items

Description: Creates a new item for the authenticated user.
Headers: Authorization: Bearer <jwt_token>
Request Body:{
"type": "PROJECT",
"title": "My Project",
"description": "A test project",
"finished": false
}


Response (200 OK):{
"id": "68476f0ac2c460d0773b8c63",
"type": "PROJECT",
"title": "My Project",
"description": "A test project",
"finished": false,
"userId": "68476a31c2c460d0773b8c61"
}


Errors: 400 Bad Request, 401 Unauthorized.


PATCH /items/{id}

Description: Updates an item by ID for the authenticated user (partial update).
Path Param: id (MongoDB ObjectId)
Headers: Authorization: Bearer <jwt_token>
Request Body:{
"title": "Updated Project",
"finished": true
}


Response (200 OK):{
"id": "68476f0ac2c460d0773b8c63",
"type": "PROJECT",
"title": "Updated Project",
"description": "A test project",
"finished": true,
"userId": "68476a31c2c460d0773b8c61"
}


Errors: 401 Unauthorized, 404 Not Found.


DELETE /items/{id}

Description: Deletes an item by ID for the authenticated user.
Path Param: id (MongoDB ObjectId)
Headers: Authorization: Bearer <jwt_token>
Response (200 OK):{
"success": true
}


Errors: 401 Unauthorized, 404 Not Found.


DELETE /items/reset

Description: Deletes all items from the items collection.
Headers: Authorization: Bearer <jwt_token>
Response (200 OK):{
"success": true
}


Errors: 401 Unauthorized.



Security

JWT Authentication: All /items endpoints and DELETE /auth/reset require a valid JWT token in the Authorization: Bearer <token> header. Tokens are generated via /auth/register or /auth/login and expire after 10 hours (configurable via spring.security.jwt.expiration in application.properties).
MongoDB: The SPRING_DATA_MONGODB_URI must include a URL-encoded password and connect to the kata3 database. Ensure your IP is whitelisted in MongoDB Atlas.
Sensitive Data: Store SPRING_DATA_MONGODB_URI and JWT_SECRET in environment variables or a .env file (gitignored).

Testing

Local Testing:

Use Insomnia with the provided collection (Kata3_API_Insomnia_Collection.json).
Steps:
Register a user (POST /auth/register).
Copy the JWT token and set it in the Insomnia environment (jwt_token).
Test /items endpoints with the Authorization header.
Use DELETE /auth/reset and DELETE /items/reset to clear data.




Troubleshooting:

401 Unauthorized: Regenerate JWT via /auth/login.
MongoDB Errors: Verify SPRING_DATA_MONGODB_URI and IP whitelisting.
Logs: Check stdout or logs for errors.



Notes

Reset Endpoints: DELETE /auth/reset and DELETE /items/reset are for development/testing. Restrict or disable in production to prevent data loss.
MongoDB: The kata3 database must exist or be created on first write. Ensure the MongoDB user has readWrite permissions.
Future Improvements:
Add role-based access for reset endpoints (e.g., admin-only).
Implement automated tests with JUnit and Mockito.
Enhance error handling for better JSON error responses.



Contributing

Fork the repository.
Create a feature branch (git checkout -b feature/xyz).
Commit changes (git commit -m "Add feature xyz").
Push to the branch (git push origin feature/xyz).
Open a pull request.

Contact
For issues or questions, contact the maintainer at [your-email@example.com] or open an issue on the repository.
