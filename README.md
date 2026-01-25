# AutoTrackPro3

A comprehensive web application for managing vehicle information, tracking service records, and maintaining detailed vehicle history.

## 📋 Description

AutoTrackPro3 is a Spring Boot-based REST API application that helps users manage their vehicles and keep track of service records. The application provides a secure, role-based system where users can register, manage their vehicles, and maintain detailed service histories.

## ✨ Features

- **User Management**
  - User registration and authentication
  - JWT-based security
  - Role-based access control
  - User profile management

- **Vehicle Management**
  - Add, update, and delete vehicles
  - Track vehicle details (make, model, year, VIN, etc.)
  - Associate vehicles with owners
  - Advanced filtering and search capabilities

- **Service Record Tracking**
  - Record detailed service history for each vehicle
  - Track service dates, descriptions, and costs
  - Filter and search service records
  - Maintain comprehensive maintenance history

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.3.5
- **Language**: Java 22
- **Build Tool**: Gradle
- **Database**: MySQL (Production), H2 (Testing)
- **Security**: Spring Security with JWT (JSON Web Tokens)
- **ORM**: Spring Data JPA / Hibernate
- **Template Engine**: Thymeleaf
- **API Documentation**: Spring REST Docs (Asciidoctor)
- **Testing**: JUnit 5, Spring Boot Test

### Key Dependencies

- `spring-boot-starter-data-jpa` - Database operations
- `spring-boot-starter-security` - Authentication & authorization
- `spring-boot-starter-web` - REST API functionality
- `spring-boot-starter-validation` - Input validation
- `io.jsonwebtoken` - JWT token management
- `lombok` - Reduces boilerplate code
- `mysql-connector-j` - MySQL database driver

## 📁 Project Structure

```
AutoTrackPro3/
├── src/
│   ├── main/
│   │   ├── java/gr/dimitriosdrakopoulos/projects/AutoTrackPro3/
│   │   │   ├── authentication/      # JWT and authentication filters
│   │   │   ├── config/              # Application configuration
│   │   │   ├── core/                # Core utilities, filters, exceptions
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── mapper/              # Entity-DTO mappers
│   │   │   ├── model/               # Entity classes (User, Vehicle, ServiceRecord)
│   │   │   ├── repository/          # Data access layer
│   │   │   ├── rest/                # REST Controllers
│   │   │   └── service/             # Business logic layer
│   │   └── resources/
│   │       ├── application.properties           # Main configuration
│   │       ├── application-dev.properties       # Development profile
│   │       ├── application-test.properties      # Test profile
│   │       ├── application-prod.properties      # Production profile
│   │       └── static/                          # Static resources
│   └── test/                        # Test classes
├── build.gradle                     # Gradle build configuration
└── gradlew / gradlew.bat           # Gradle wrapper scripts
```

## 🚀 Getting Started

### Prerequisites

- Java 22 or higher
- MySQL Server 8.0+
- Gradle (wrapper included)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd AutoTrackPro3
   ```

2. **Configure the database**
   
   Create a MySQL database:
   ```sql
   CREATE DATABASE servicebook3db;
   CREATE USER 'servicebookuser'@'localhost' IDENTIFIED BY '12345';
   GRANT ALL PRIVILEGES ON servicebook3db.* TO 'servicebookuser'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Configure environment variables (optional)**
   
   You can override default database settings using environment variables:
   - `MYSQL_HOST` (default: localhost)
   - `MYSQL_PORT` (default: 3306)
   - `MYSQL_DB` (default: servicebook3db)
   - `MYSQL_USER` (default: servicebookuser)
   - `MYSQL_PASSWORD` (default: 12345)

4. **Build the application**
   ```bash
   ./gradlew build
   ```

### Running the Application

#### Development Mode
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

Or on Windows:
```cmd
gradlew.bat bootRun --args="--spring.profiles.active=dev"
```

#### Production Mode
```bash
./gradlew bootRun --args='--spring.profiles.active=prod'
```

The application will start on `http://localhost:8080` (or configured port).

### Running Tests

```bash
./gradlew test
```

## 🔐 Security

The application uses JWT (JSON Web Token) for authentication. To access protected endpoints:

1. Register a new user via the registration endpoint
2. Authenticate using login credentials to receive a JWT token
3. Include the JWT token in the `Authorization` header for subsequent requests:
   ```
   Authorization: Bearer <your-jwt-token>
   ```

## 📚 API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Authenticate and receive JWT token

### Users
- `GET /api/users` - List all users (with filtering)
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Vehicles
- `GET /api/vehicles` - List all vehicles (with filtering)
- `GET /api/vehicles/{id}` - Get vehicle by ID
- `POST /api/vehicles` - Create new vehicle
- `PUT /api/vehicles/{id}` - Update vehicle
- `DELETE /api/vehicles/{id}` - Delete vehicle

### Service Records
- `GET /api/service-records` - List all service records (with filtering)
- `GET /api/service-records/{id}` - Get service record by ID
- `POST /api/service-records` - Create new service record
- `PUT /api/service-records/{id}` - Update service record
- `DELETE /api/service-records/{id}` - Delete service record

## ⚙️ Configuration

### Application Profiles

The application supports multiple profiles:

- **dev** - Development environment (default)
- **test** - Testing environment
- **prod** - Production environment

### Database Configuration

Database settings can be configured in the profile-specific properties files:
- [src/main/resources/application-dev.properties](src/main/resources/application-dev.properties)
- [src/main/resources/application-test.properties](src/main/resources/application-test.properties)
- [src/main/resources/application-prod.properties](src/main/resources/application-prod.properties)

### File Upload Limits

- Maximum file size: 5MB
- Maximum request size: 10MB

These can be adjusted in [application.properties](src/main/resources/application.properties).

## 🧪 Testing

The project includes comprehensive test coverage with:
- Unit tests for services and business logic
- Integration tests for REST controllers
- H2 in-memory database for testing
- Spring REST Docs for API documentation generation

## 📖 Documentation

API documentation is automatically generated using Spring REST Docs. After running tests, the documentation can be found in:
```
build/generated-snippets/
```

Generate the full documentation:
```bash
./gradlew asciidoctor
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the terms specified in the project documentation.

## 👤 Author

**Dimitrios Drakopoulos**
- GitHub: [@dimitriosdrakopoulos](https://github.com/dimitriosdrakopoulos)

## 🐛 Known Issues

- None currently reported

## 📅 Version History

- **0.0.1-SNAPSHOT** - Initial development version

## 📞 Support

For support, please open an issue in the GitHub repository.

---

Built with ❤️ using Spring Boot
