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
  - Track service dates, descriptions, costs, odometer readings
  - Log warranty information and maintenance recommendations
  - Track next scheduled service dates and odometer thresholds
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
- `spring-boot-starter-thymeleaf` - Template rendering
- `thymeleaf-extras-springsecurity6` - Security integration with Thymeleaf
- `io.jsonwebtoken:jjwt` - JWT token management (API, Implementation, Jackson)
- `lombok` - Reduces boilerplate code
- `mysql-connector-j` - MySQL database driver
- `spring-restdocs-mockmvc` - API documentation generation
- `h2database` - In-memory database for testing

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
``` with role-based access control. 

### User Roles

- **SUPER_ADMIN** - Full system access
- **OWNER** - Can own vehicles and manage their service records
- **DRIVER** - Can be assigned to vehicles as drivers

### Authentication Flow

1. Register a new user via the registration endpoint with appropriate role
2. Authenticate using login credentials to receive a JWT token
3. Include the JWT token in the `Authorization` header for subsequent requests:
   ```
   Authorization: Bearer <your-jwt-token>
   ```

### Vehicle Access Control

- Only owners and drivers with proper authorization can access/manage vehicles
- Vehicles must have at least one owner
- SUPER_ADMIN users cannot be assigned as vehicle owners or drivers
- Only users with OWNER role can be vehicle owners
- Only users with DRIVER role can be vehicle drivershenticate using login credentials to receive a JWT token
3. Include the JWT token in the `Authorization` header for subsequent requests:
   ```
   Authorization: Bearer <your-jwt-token>
   ```
/save` - Create new vehicle
- `PATCH /api/vehicles/update/{id}` - Update vehicle
- `DELETE /api/vehicles/delete
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
- `GET /api/serviceRecords` - List all service records (with filtering)
- `GET /api/serviceRecords/{id}` - Get service record by ID
- `POST /api/seviceRecords/save` - Create new service record
- `PATCH /api/serviceRecords/update/{id}` - Update service record
- `DELETE /api/serviceRecords/delete` - Delete service record

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

## �️ Data Models

### Vehicle Model
- **vin** - Vehicle Identification Number (unique, required)
- **licencePlate** - License plate (unique)
- **make** - Vehicle manufacturer
- **model** - Vehicle model name
- **type** - VehicleType enum (CAR, TRUCK, MOTORCYCLE, etc.)
- **color** - Color enum (BLACK, WHITE, BLUE, RED, SILVER, GRAY, BROWN, ORANGE, GREEN, YELLOW, PURPLE, PINK, BEIGE, GOLD)
- **productionDate** - Manufacturing date
- **fuel** - Fuel type enum (PETROL, DIESEL, HYBRID, ELECTRIC)
- **gearbox** - Gearbox type enum (MANUAL, AUTOMATIC)
- **odometer** - Current mileage (Long)
- **owners** - List of users with OWNER role
- **drivers** - List of users with DRIVER role
- **serviceRecords** - Associated service records

### Service Record Model
- **vehicleId** - Associated vehicle
- **dateOfService** - Service date
- **serviceType** - Type of service performed
- **description** - Service details
- **odometer** - Odometer reading at service (Long)
- **parts** - Parts used/replaced
- **cost** - Service cost
- **nextService** - Next scheduled service date (optional)
- **nextServiceOdometer** - Odometer threshold for next service (optional, Long)
- **recommendations** - Maintenance recommendations (optional)
- **warranty** - Whether warranty applies (Boolean, optional)
- **warrantyInfo** - Warranty details (optional)

### User Model
- **username** - Unique username
- **password** - Encrypted password
- **firstname** - User's first name
- **lastname** - User's last name
- **email** - User email address
- **phonenumber** - Contact number
- **gender** - Gender enum (MALE, FEMALE, OTHER)
- **roleType** - Role enum (SUPER_ADMIN, OWNER, DRIVER)
- **isActive** - Account status
- **driverLicence** - Driver license number
- **licenceExpiration** - License expiration date
- **licenceCategory** - License category
- **identityNumber** - Identity/ID number
- **city** - City of residence
- **ownedVehicles** - Vehicles owned
- **drivenVehicles** - Vehicles driven

## �📝 License

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
