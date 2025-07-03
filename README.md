# StudentTracker

A Java backend for managing students, with REST API, JWT authentication, robust unit tests, and ready for integration with a JavaFX graphical interface.

---

## Requirements

- **Java 17 or higher** (Java 17 required for tests with Mockito)
- **Maven** (for building and running the project)
- **PostgreSQL** (tested with PostgreSQL 13+)
- **A `db.properties` file** (see below for a sample)

---

## Project Description

StudentTracker is a Java-based RESTful backend for managing student records. It supports user registration and login (with password hashing and JWT authentication), CRUD operations for students, advanced API documentation (OpenAPI/Swagger), and robust unit tests. The project is structured for easy extension with a JavaFX GUI and advanced features like search, sorting, statistics, and reporting.

---

## Project Structure

```
StudentTracker/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/laplateforme/tracker/
│   │           ├── dao/         # Data Access Objects (StudentDAO, UserDAO)
│   │           ├── handler/     # HTTP Handlers for REST endpoints
│   │           ├── model/       # Data models (Student, User)
│   │           ├── service/     # Business logic (StudentService, UserService)
│   │           └── util/        # Utilities (DatabaseManager, JwtUtil)
│   └── test/
│       └── java/
│           └── com/laplateforme/tracker/
│               ├── dao/        # DAO unit tests
│               └── service/    # Service layer tests
├── docs/
│   └── openapi.yaml            # OpenAPI/Swagger API documentation
├── run-server.sh               # Script to build and run the backend server
├── run-test.sh                 # Script to run all tests with Java 17
├── .env                        # Environment variables (e.g., JWT_SECRET)
├── pom.xml                     # Maven project file (inside StudentTracker/)
└── README.md                   # This file
```

---

## How to Compile and Run the Project

1. **Navigate to the Maven project directory:**
   ```sh
   cd StudentTracker
   ```
2. **Compile the project:**
   ```sh
   mvn clean package -DskipTests
   ```
   (Make sure you are in the StudentTracker directory containing the pom.xml)
3. **Run the server:**
   ```sh
   ./run-server.sh
   ```
   This script will build and start the server with the correct classpath.

---

## Project Functionalities & API Overview

- **User Registration:** `POST /register` (username, password)
- **User Login:** `POST /login` (returns JWT token)
- **JWT Authentication:** All /students endpoints require a valid JWT in the Authorization header.
- **Student CRUD:**
  - `GET /students` (list all students)
  - `POST /students` (create student)
  - `GET /students/{id}` (get student by ID)
  - `PUT /students/{id}` (update student)
  - `DELETE /students/{id}` (delete student)
- **API Documentation:** See `docs/openapi.yaml` (viewable in Swagger UI)

---

## How to Use curl to Test API Endpoints

### 1. Register a User
```sh
curl -i -X POST http://localhost:8080/register \
  -H "Content-Type: application/json" \
  -d '{"username": "user1", "password": "pass123"}'
```

### 2. Login and Get JWT Token
```sh
curl -i -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"username": "user3", "password": "pass123"}'
```
Copy the `token` value from the response.

### 3. Use JWT Token for Protected Endpoints
```sh
curl -i -X GET http://localhost:8080/students \
  -H "Authorization: Bearer <your_token>"
```
Replace `<your_token>` with the token from the login response.

### 4. Create a Student
```sh
curl -i -X POST http://localhost:8080/students \
  -H "Authorization: Bearer <your_token>" \
  -H "Content-Type: application/json" \
  -d '{"firstName": "Laure", "lastName": "Delaure", "age": 18, "grade": 18.5}'
```

### 5. Update, Delete, or Get Student by ID
(See OpenAPI docs for details)

---

## How to Run Tests

- **Unit and service tests:**
  ```sh
  ./run-test.sh
  ```
  This script will:
  - Switch to Java 17 (required for Mockito)
  - Run all tests with detailed output

- **Manual Maven test run:**
  ```sh
  cd StudentTracker
  mvn test -Dsurefire.printSummary=true -Dsurefire.useFile=false
  ```

---

## Environment Variables

- Set your JWT secret in a `.env` file in the project root:
  ```
  JWT_SECRET=my-secret-key
  ```
- The server will use this secret for signing JWT tokens.

---

## Database Configuration (db.properties)

Create a file named `db.properties` in `StudentTracker/src/main/resources/` with the following content:

```
jdbc.url=jdbc:postgresql://localhost:5432/student_tracker
jdbc.user=postgres
jdbc.password=postgres
```

- Adjust the values as needed for your local PostgreSQL setup.
- The database and user must exist before running the server.

---

## Setting Up PostgreSQL Locally

You can use the following script to set up PostgreSQL, create the database and user, and grant privileges. This example assumes you have `psql` installed and superuser access (e.g., via `postgres` user):

```sh
#!/bin/bash
# setup-postgres.sh
# Usage: bash setup-postgres.sh

DB_NAME=student_tracker
DB_USER=postgres
DB_PASS=postgres

# Create database user (if not exists)
sudo -u postgres psql -c "DO $$ BEGIN IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = '$DB_USER') THEN CREATE ROLE $DB_USER LOGIN PASSWORD '$DB_PASS'; END IF; END $$;"

# Create database (if not exists)
sudo -u postgres psql -c "CREATE DATABASE $DB_NAME OWNER $DB_USER;"

# Grant all privileges
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER;"
```

- Save this as `setup-postgres.sh` and run with `bash setup-postgres.sh` (you may need `sudo` depending on your system).
- Edit `DB_USER` and `DB_PASS` as needed to match your `db.properties`.
- If you use a different user/password, update both the script and your `db.properties`.

---

## API Documentation

- See `docs/openapi.yaml` for a full OpenAPI/Swagger spec.
- You can view this file in [Swagger Editor](https://editor.swagger.io/) or import it into Swagger UI/Postman.

---

## Next Steps

- Integrate a JavaFX GUI client
- Add advanced features (search, sorting, statistics, charts, CSV export)
- Add end-to-end tests

---

