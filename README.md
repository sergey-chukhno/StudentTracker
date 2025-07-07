# StudentTracker

A full-stack Java application for managing students, featuring a modular backend (REST API, JWT authentication, unitary tests), a modern JavaFX GUI, a complete HTML/CSS/JS presentation, and complete project documentation.

---

## Requirements

- **Java 17 or higher**
- **Maven**
- **PostgreSQL** (tested with PostgreSQL 13+)
- **A `db.properties` file** (see below)

---

## Project Overview

StudentTracker is a complete Java solution for student management:
- **Backend:** RESTful API, JWT authentication, CRUD, analytics, unit tests (JUnit/Mockito), modular architecture (DAO, Service, Handler, Model, Util)
- **Frontend:** JavaFX GUI (FXML, MVC, search, sorting, PDF export, analytics)
- **Presentation:** Animated HTML/CSS/JS slides in the `presentation/` folder
- **Documentation:** Up-to-date diagrams and specs in the `documentation/` folder
- **Deployment:** Ready for cloud deployment (Render.com)

---

## Project Structure

```
StudentTracker/
├── backend/                  # Backend Java (API, logic, tests)
├── gui/                      # JavaFX GUI (FXML, controllers, assets)
├── documentation/            # Diagrams, OpenAPI, architecture docs
├── presentation/             # HTML/CSS/JS animated project presentation
├── run-server.sh             # Script to build and run the backend server
├── run-test.sh               # Script to run all tests
├── setup-postgres.sh         # Script to set up PostgreSQL
├── .gitignore                # Now ignores test reports
├── pom.xml                   # Maven project file
└── README.md                 # This file
```

---

## Key Features

- **User Registration & Login** (`/register`, `/login`)
- **JWT Authentication** (all /students endpoints)
- **Student CRUD** (`/students`, `/students/{id}`)
- **Analytics & PDF Export**
- **Search & Sorting** (frontend)
- **Modern JavaFX GUI** (MVC, FXML, CSS)
- **Comprehensive Tests** (JUnit, Mockito, curl for API)
- **Professional Documentation** (class diagrams, architecture, OpenAPI)
- **Animated HTML/CSS/JS Presentation**
- **Cloud Deployment Ready** (Render.com)

---

## Documentation & Diagrams

- All up-to-date documentation, diagrams, and OpenAPI spec are in the `documentation/` folder:
  - `class-diagram.png`, `overview-architecture.png`, `backend-architecture.png`, `frontend-gui-architecture.png`, `backend-frontend-integration.png`
  - `openapi.yaml` (API spec)
  - Markdown explanations for each diagram

---

## Project Presentation

- See the `presentation/` folder for a complete, animated HTML/CSS/JS presentation of the project.
- Open `presentation/index.html` in your browser to view slides on architecture, features, tests, deployment, demo, and more.

---

## How to Compile and Run the Project

1. **Backend:**
   ```sh
   ./run-server.sh
   ```
   (or use Maven: `cd backend && mvn clean package && java -jar target/StudentTracker-1.0-SNAPSHOT.jar`)

2. **Frontend (GUI):**
   ```sh
   cd gui && mvn clean package && java -jar target/gui-1.0-SNAPSHOT.jar
   ```
   (Configure the backend URL in the GUI if needed)

---

## How to Test the API (with curl)

- See the README section above or the OpenAPI spec in `documentation/openapi.yaml` for endpoint details.
- Example:
  ```sh
  curl -i -X POST http://localhost:8080/register \
    -H "Content-Type: application/json" \
    -d '{"username": "user1", "password": "pass123"}'
  ```

---

## How to Run Tests

- **All tests:**
  ```sh
  ./run-test.sh
  ```
- **Manual Maven test run:**
  ```sh
  cd backend && mvn test
  ```

---

## Deployment (Render.com)

- See the deployment slide in the presentation and the `documentation/` folder for a step-by-step guide.
- Main steps:
  - Prepare backend for cloud (env vars, config)
  - Set up PostgreSQL on Render
  - Configure build/start commands
  - Deploy and test API (curl)
  - Distribute GUI locally

---

## Environment Variables

- Set your JWT secret and database credentials as environment variables or in `.env`/`db.properties`.
- Example:
  ```
  JWT_SECRET=my-secret-key
  JDBC_URL=jdbc:postgresql://localhost:5432/student_tracker
  JDBC_USER=postgres
  JDBC_PASSWORD=postgres
  ```

---

## .gitignore and Test Reports

- The `.gitignore` now includes:
  ```
  **/target/surefire-reports/
  ```
- This prevents Maven test reports from being tracked by git or pushed to GitHub.
- If you have already committed these files, remove them and commit the change.

---

## Authors & License

- See LICENSE for details.

