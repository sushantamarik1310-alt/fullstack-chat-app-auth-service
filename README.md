# Auth Service - Spring Boot Microservice

Authentication microservice for the Fullstack Chat App using Spring Boot and MongoDB.

## Features

- User Registration (SignUp)
- User Login
- JWT Token Generation & Verification
- Bcrypt Password Encryption
- MongoDB Integration
- CORS Enabled
- RESTful API

## Tech Stack

- **Framework**: Spring Boot 3.1.5
- **Language**: Java 17
- **Database**: MongoDB
- **Authentication**: JWT (JSON Web Tokens)
- **Password Encoding**: BCrypt
- **Build Tool**: Maven

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MongoDB Atlas account (or local MongoDB)

## Setup Instructions

### 1. Clone/Navigate to the project
```bash
cd auth-service
```

### 2. Configure Environment Variables
Update `src/main/resources/application.properties`:
```properties
spring.data.mongodb.uri=your_mongodb_connection_string
jwt.secret.key=your_secret_key
jwt.expiration.time=86400000
cloudinary.cloud.name=your_cloud_name
cloudinary.api.key=your_api_key
cloudinary.api.secret=your_api_secret
```

### 3. Build the project
```bash
mvn clean install
```

### 4. Run the application
```bash
mvn spring-boot:run
```

The service will start on **http://localhost:5001**

## API Endpoints

### SignUp
```http
POST /api/auth/signup
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "fullName": "John Doe"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "user": {
    "id": "507f1f77bcf86cd799439011",
    "email": "user@example.com",
    "fullName": "John Doe",
    "createdAt": "2026-03-15T10:30:00"
  },
  "message": "User registered successfully"
}
```

### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "user": {
    "id": "507f1f77bcf86cd799439011",
    "email": "user@example.com",
    "fullName": "John Doe"
  },
  "message": "Login successful"
}
```

### Verify Token
```http
POST /api/auth/verify-token
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### Get User by ID
```http
GET /api/auth/user/{userId}
```

## Project Structure

```
auth-service/
├── src/main/java/com/chatapp/auth/
│   ├── controller/
│   │   └── AuthController.java
│   ├── service/
│   │   └── AuthService.java
│   ├── model/
│   │   └── User.java
│   ├── repository/
│   │   └── UserRepository.java
│   ├── dto/
│   │   ├── SignUpRequest.java
│   │   ├── LoginRequest.java
│   │   └── AuthResponse.java
│   ├── security/
│   │   ├── JwtUtil.java
│   │   └── SecurityConfig.java
│   └── AuthServiceApplication.java
├── src/main/resources/
│   └── application.properties
├── pom.xml
└── README.md
```

## Key Classes

- **AuthController**: REST endpoints for authentication
- **AuthService**: Business logic for signup/login
- **User**: MongoDB document model
- **JwtUtil**: JWT token generation and validation
- **SecurityConfig**: Security configuration with BCrypt and CORS

## Integration with Frontend

Update your frontend API calls:
```javascript
const API_URL = 'http://localhost:5001/api';

// SignUp
fetch(`${API_URL}/auth/signup`, {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ email, password, fullName })
});

// Login
fetch(`${API_URL}/auth/login`, {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ email, password })
});
```

## Next Steps

1. Deploy this service to a cloud provider (Heroku, AWS, GCP)
2. Create API Gateway to route requests
3. Create Chat Service (Node.js)
4. Update frontend to use the new API endpoints

## License

MIT
