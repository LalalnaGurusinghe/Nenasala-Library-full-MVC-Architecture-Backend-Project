# Library Management System

A robust Spring Boot-based library management system with JWT authentication, role-based access control, and comprehensive book management features.

## 📋 Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Security](#security)
- [Usage Examples](#usage-examples)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

## ✨ Features

- **🔐 JWT Authentication**: Secure stateless authentication with token-based access
- **👥 Role-Based Access Control**: Separate permissions for Admin and User roles
- **📚 Book Management**: Complete CRUD operations for book inventory
- **📖 Issue/Return System**: Track book borrowing and returns with automatic availability updates
- **🔄 Automatic Inventory Management**: Real-time book quantity and availability tracking
- **📊 Database Integration**: MySQL database with JPA/Hibernate ORM
- **🛡️ Spring Security**: Comprehensive security implementation
- **📝 RESTful API**: Clean, well-structured REST endpoints
- **🔍 H2 Console**: Database inspection tool for development

## 🛠️ Technology Stack

- **Backend Framework**: Spring Boot 3.x
- **Security**: Spring Security with JWT
- **Database**: MySQL 8.0
- **ORM**: JPA/Hibernate
- **Build Tool**: Maven
- **Java Version**: 21
- **Authentication**: JWT (JSON Web Tokens)
- **Password Encoding**: BCrypt

## 📁 Project Structure

```
Librarymanagement/
├── controller/                 # REST API Controllers
│   ├── AdminController.java   # Admin-specific endpoints
│   ├── AuthController.java    # Authentication endpoints
│   ├── BookController.java    # Book management endpoints
│   └── IssueController.java   # Book issue/return endpoints
├── dto/                       # Data Transfer Objects
│   ├── BookDTO.java          # Book data transfer
│   ├── LoginRequestDTO.java  # Login request data
│   ├── LoginResponseDTO.java # Login response data
│   └── RegisterRequestDTO.java # Registration data
├── entity/                    # JPA Entities
│   ├── Book.java             # Book entity
│   ├── IssueRecord.java      # Issue record entity
│   └── User.java             # User entity
├── JWT/                       # JWT Implementation
│   ├── JWTAuthenticationFilter.java # JWT filter
│   └── JWTService.java       # JWT utilities
├── repo/                      # Repository Layer
│   ├── BookRepo.java         # Book repository
│   ├── IssueRecordRepo.java  # Issue record repository
│   └── UserRepo.java         # User repository
├── security/                  # Security Configuration
│   └── SecurityConfig.java   # Spring Security config
├── service/                   # Business Logic Layer
│   ├── AuthenticationService.java # Authentication logic
│   ├── BookService.java      # Book business logic
│   ├── CustomUserDetailsService.java # User details service
│   └── IssueRecordService.java # Issue/return logic
├── LibrarymanagementApplication.java # Main application class
└── application.properties     # Application configuration
```

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- **Java 21** or higher
- **Maven 3.6** or higher
- **MySQL 8.0** or higher
- **Git** (for cloning the repository)

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone <repository-url>
cd Librarymanagement
```

### 2. Database Setup

Create a MySQL database:

```sql
CREATE DATABASE nanasalaDB;
```

### 3. Configure Database Connection

Update `src/main/resources/application.properties` with your database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/nanasalaDB?createDatabaseIfNotExist=true
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 4. Build and Run

```bash
# Clean and compile
mvn clean compile

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api`

## ⚙️ Configuration

### Application Properties

Key configuration options in `application.properties`:

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/api

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/nanasalaDB?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=12345#Lmg

# JWT Configuration
jwt.secretKey=your-secret-key
jwt.expiration=3600000  # 1 hour in milliseconds

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Environment Variables

You can override these properties using environment variables:

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/nanasalaDB
export SPRING_DATASOURCE_USERNAME=your_username
export SPRING_DATASOURCE_PASSWORD=your_password
export JWT_SECRET_KEY=your-secret-key
```

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Authentication Endpoints

#### Register Normal User
```http
POST /auth/register/normal
Content-Type: application/json

{
  "username": "john",
  "email": "john@example.com",
  "password": "password123"
}
```

#### Login
```http
POST /auth/login
Content-Type: application/json

{
  "username": "john",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "john",
  "roles": ["ROLE_USER"]
}
```

### Book Management Endpoints

#### Get All Books
```http
GET /books/all
Authorization: Bearer <token>
```

#### Get Book by ID
```http
GET /books/{id}
Authorization: Bearer <token>
```

#### Add Book (Admin Only)
```http
POST /books/add
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "title": "The Great Gatsby",
  "author": "F. Scott Fitzgerald",
  "isbn": "978-0743273565",
  "quantity": 5,
  "isAvailable": true
}
```

#### Update Book (Admin Only)
```http
POST /books/update/{id}
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "quantity": 4
}
```

#### Delete Book (Admin Only)
```http
DELETE /books/delete/{id}
Authorization: Bearer <admin_token>
```

### Issue/Return Endpoints

#### Issue a Book
```http
POST /issuerecords/issue/{bookId}
Authorization: Bearer <token>
```

#### Return a Book
```http
POST /issuerecords/return/{issueRecordId}
Authorization: Bearer <token>
```

### Admin Endpoints

#### Register Admin User
```http
POST /admin/register/admin
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "username": "admin2",
  "email": "admin2@library.com",
  "password": "admin123"
}
```

## 🗄️ Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);
```

### User Roles Table
```sql
CREATE TABLE users_roles (
    user_id BIGINT,
    roles VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Books Table
```sql
CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    is_available BOOLEAN NOT NULL
);
```

### Issue Records Table
```sql
CREATE TABLE isserecords (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    is_returned BOOLEAN NOT NULL,
    user_id BIGINT,
    book_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);
```

## 🔐 Security

### Authentication Flow

1. **Registration**: Users register with username, email, and password
2. **Login**: Users authenticate and receive JWT token
3. **Authorization**: JWT token is validated for each request
4. **Role-Based Access**: Endpoints are protected based on user roles

### Security Features

- **JWT Token Authentication**: Stateless authentication
- **BCrypt Password Encoding**: Secure password hashing
- **Role-Based Access Control**: ADMIN and USER roles
- **CSRF Protection**: Disabled for API endpoints
- **Session Management**: Stateless sessions

### JWT Token Structure

```json
{
  "header": {
    "alg": "HS256",
    "typ": "JWT"
  },
  "payload": {
    "sub": "username",
    "iat": 1234567890,
    "exp": 1234571490
  }
}
```

## 💡 Usage Examples

### Complete Workflow Example

#### 1. Create Admin User (Manual)
```sql
INSERT INTO users (username, email, password, roles) 
VALUES ('admin', 'admin@library.com', 
        '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 
        'ROLE_ADMIN,ROLE_USER');
```

#### 2. Login as Admin
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "password"}'
```

#### 3. Add Books
```bash
curl -X POST http://localhost:8080/api/books/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <admin_token>" \
  -d '{
    "title": "1984",
    "author": "George Orwell",
    "isbn": "978-0451524935",
    "quantity": 3,
    "isAvailable": true
  }'
```

#### 4. Register Normal User
```bash
curl -X POST http://localhost:8080/api/auth/register/normal \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "email": "john@example.com",
    "password": "password123"
  }'
```

#### 5. Issue Book
```bash
curl -X POST http://localhost:8080/api/issuerecords/issue/1 \
  -H "Authorization: Bearer <user_token>"
```

## 🧪 Testing

### Manual Testing

Use the provided curl commands or tools like Postman to test the API endpoints.

### Automated Testing

```bash
# Run tests
mvn test

# Run with coverage
mvn jacoco:report
```

### Database Testing

Access H2 Console (if using H2 for testing):
```
http://localhost:8080/api/h2-console
```

## 🔧 Troubleshooting

### Common Issues

#### 1. Database Connection Error
```
Error: Communications link failure
```
**Solution**: Check MySQL service is running and credentials are correct.

#### 2. JWT Token Expired
```
Error: JWT token expired
```
**Solution**: Re-login to get a new token.

#### 3. 404 Error on Endpoints
```
Error: 404 Not Found
```
**Solution**: Ensure you're using the correct base URL: `http://localhost:8080/api`

#### 4. Permission Denied
```
Error: 403 Forbidden
```
**Solution**: Check if you have the required role (ADMIN for admin endpoints).

### Logs

Enable debug logging in `application.properties`:
```properties
logging.level.com.example.Librarymanagement=DEBUG
logging.level.org.springframework.security=DEBUG
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines

- Follow Java coding conventions
- Add unit tests for new features
- Update documentation for API changes
- Use meaningful commit messages

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For support and questions:

- Create an issue in the repository
- Contact: [lalanagurusinghe@gmail.com]


---

**Note**: This is a development version. For production deployment, ensure proper security configurations and environment-specific settings.
