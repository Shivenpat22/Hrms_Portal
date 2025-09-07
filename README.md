# HR Management System (HRMS)

A comprehensive web-based HR management system built with Spring Boot, Thymeleaf, and MySQL for managing employees, departments, and job roles.

## Features

### Core Modules
- **Employee Management**: Complete CRUD operations for employee records
- **Department Management**: Organize and manage company departments
- **Role Management**: Define and manage job roles and responsibilities

### Admin Features
- **Authentication**: Secure login/logout with Spring Security
- **Dashboard**: Statistics overview with total counts
- **Role-based Access**: Only authenticated admins can access management features

### Advanced Features
- **Employee Search**: Search by name, email, or department
- **Pagination**: Efficient data display with pagination
- **Sorting**: Sort employees by various criteria
- **File Upload**: Employee photo upload functionality
- **Validation**: Comprehensive form validation
- **Audit Trail**: Created/updated timestamps for all entities

## Tech Stack

- **Backend**: Spring Boot 3.2.0
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA with Hibernate
- **Template Engine**: Thymeleaf
- **Security**: Spring Security
- **Frontend**: Bootstrap 5.3.0, Font Awesome 6.4.0
- **Build Tool**: Maven
- **Java Version**: 17

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd hr-management-system
```

### 2. Database Setup
1. Create a MySQL database:
```sql
CREATE DATABASE hrms_db;
```

2. Update database configuration in `src/main/resources/application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Build and Run
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 4. Default Admin Credentials
- **Username**: admin
- **Password**: admin123

## Project Structure

```
src/
├── main/
│   ├── java/com/hrms/
│   │   ├── config/           # Configuration classes
│   │   ├── controller/       # REST controllers
│   │   ├── entity/          # JPA entities
│   │   ├── repository/      # Data access layer
│   │   └── service/         # Business logic layer
│   └── resources/
│       ├── templates/       # Thymeleaf templates
│       └── application.properties
```

## API Endpoints

### Public Pages
- `GET /` - Main dashboard
- `GET /login` - Login page
- `GET /access-denied` - Access denied page

### Admin Pages (Requires Authentication)
- `GET /admin/dashboard` - Admin dashboard
- `GET /employees` - Employee list
- `GET /employees/new` - Add employee form
- `GET /employees/{id}` - View employee
- `GET /employees/{id}/edit` - Edit employee form
- `POST /employees/new` - Create employee
- `POST /employees/{id}/edit` - Update employee
- `POST /employees/{id}/delete` - Delete employee
- `GET /departments` - Department list
- `GET /departments/new` - Add department form
- `GET /departments/{id}` - View department
- `GET /departments/{id}/edit` - Edit department form
- `POST /departments/new` - Create department
- `POST /departments/{id}/edit` - Update department
- `POST /departments/{id}/delete` - Delete department
- `GET /roles` - Role list
- `GET /roles/new` - Add role form
- `GET /roles/{id}` - View role
- `GET /roles/{id}/edit` - Edit role form
- `POST /roles/new` - Create role
- `POST /roles/{id}/edit` - Update role
- `POST /roles/{id}/delete` - Delete role

## Entity Relationships

### Employee
- **ManyToOne** with Department
- **ManyToOne** with Role
- Fields: id, name, email, mobile, dob, joiningDate, photoUrl, createdAt, updatedAt

### Department
- **OneToMany** with Employee
- Fields: id, name, description, createdAt, updatedAt

### Role
- **OneToMany** with Employee
- Fields: id, title, description, createdAt, updatedAt

### User
- Used for authentication
- Fields: id, username, password, email, fullName, active, role, createdAt, updatedAt

## Features in Detail

### Employee Management
- Create, read, update, delete employee records
- Search employees by name, email, or department
- Pagination and sorting
- Photo upload functionality
- Validation for unique email and mobile numbers
- Date validation (DOB must be in past)

### Department Management
- Create, read, update, delete departments
- Prevent deletion if department has employees
- Unique department names
- Description field for additional details

### Role Management
- Create, read, update, delete roles
- Prevent deletion if role has employees
- Unique role titles
- Description field for role responsibilities

### Security Features
- Spring Security integration
- Password encryption with BCrypt
- Session management
- Access control for all management operations
- Default admin user creation

### UI/UX Features
- Responsive Bootstrap design
- Modern gradient styling
- Font Awesome icons
- Form validation with error messages
- Success/error notifications
- Confirmation dialogs for deletions
- Sidebar navigation

## Database Schema

The application uses JPA/Hibernate to automatically generate the database schema. Tables include:

- `employees` - Employee records
- `departments` - Department information
- `roles` - Job role definitions
- `users` - Admin user accounts

## Configuration

Key configuration options in `application.properties`:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/hrms_db
spring.datasource.username=root
spring.datasource.password=password

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Security
spring.security.user.name=admin
spring.security.user.password=admin123
```

## Development

### Adding New Features
1. Create entity classes in `entity` package
2. Create repository interfaces in `repository` package
3. Create service classes in `service` package
4. Create controller classes in `controller` package
5. Create Thymeleaf templates in `resources/templates`

### Testing
```bash
# Run tests
mvn test

# Run with coverage
mvn jacoco:report
```

## Deployment

### Production Deployment
1. Update `application.properties` for production database
2. Set `spring.jpa.hibernate.ddl-auto=validate`
3. Configure proper security settings
4. Build JAR file: `mvn clean package`
5. Run: `java -jar target/hr-management-system-0.0.1-SNAPSHOT.jar`










