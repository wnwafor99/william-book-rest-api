**Book & Author REST API**

A RESTful backend service for managing books and authors, built with Spring Boot, PostgreSQL, Docker, and deployed using AWS.
The API supports full CRUD operations, pagination, and clean separation between persistence and API models.

** Features**

Create, read, update, and delete Books and Authors

Pagination and sorting for large datasets

DTO-based API design with mapper layer

Proper HTTP response handling using ResponseEntity

PostgreSQL database with JPA/Hibernate

Dockerized setup for local development and deployment

Environment-variable-based secret management

** Tech Stack**

Backend: Java, Spring Boot

Database: PostgreSQL, Spring Data JPA

Containerization: Docker, Docker Compose

Cloud: AWS

Build Tool: Maven

 Project Structure
src/main/java
├── controller     # REST controllers
├── service        # Business logic
├── repository     # JPA repositories
├── domain          # JPA entities & DTOs
    ├── dto            # Data Transfer Objects
    ├── entities       # JPA entities
├── mapper         # Entity ↔ DTO mapping

**Pagination Example**

The API supports pagination and sorting using Spring Data JPA.

Example request:

GET /api/books?page=0&size=10&sort=title,asc


This returns a paginated list of books without loading all records into memory.

 **Configuration & Environment Variables**

Sensitive credentials are not hardcoded.

application.properties uses environment variables:

spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}


Create a .env file locally (not committed):

DB_USERNAME=postgres
DB_PASSWORD=postgres

 **Running Locally with Docker**
1. Start PostgreSQL
docker-compose up -d

2️. Run the application
./mvnw spring-boot:run


The API will be available at:

http://localhost:8080

📌 Why This Project?

This project was built to practice:

Backend API design

Scalable data access using pagination

Clean architecture patterns (DTOs, mappers, services)

Containerized development and cloud deployment

Secure handling of configuration and secrets

📈 Future Improvements

Authentication & authorization (JWT)

API documentation with Swagger/OpenAPI

Integration and unit testing

Caching for frequently accessed resources

👤 Author

William Nwafor
Computer Science Student
GitHub: https://github.com/wnwafor99
