# FAFSA Edit Rule Processor

A Spring Boot service that validates FAFSA (Free Application for Federal Student Aid) application data by applying configurable validation rules (edits). The service provides a REST API that accepts application data, validates it against business rules, and returns detailed validation results.

## Table of Contents

- [Overview](#overview)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Building the Service](#building-the-service)
- [Running the Service](#running-the-service)
- [Testing](#testing)
- [API Documentation](#api-documentation)

---

## Technology Stack

- **Java 17** - Programming language
- **Spring Boot 4.0.2** - Application framework
- **Gradle 9.3.0** - Build tool
- **Jackson** - JSON serialization/deserialization
- **JUnit 5** - Testing framework
- **Mockito** - Mocking framework for unit tests
- **Lombok** - Reduces boilerplate code

---

## Prerequisites

Before building and running the service, ensure you have:

- **Java 17** or higher installed
  ```bash
  java -version
  # Should show Java 17 or higher
  ```

- **Git** (to clone the repository)
  ```bash
  git --version
  ```

- **curl** or **Postman** (for testing API endpoints)

> **Note:** Gradle is included via the Gradle Wrapper (`gradlew`), so no separate Gradle installation is needed.

---

## Building the Service

### 1. Clone the Repository

```bash
git clone https://github.com/darpanshah7/gdit-technical-assignment.git
cd gdit-technical-assessment
```

### 2. Build the Project

```bash
./gradlew clean build
```

This will:
- Compile all source code
- Run all unit tests
- Generate a JAR file in `build/libs/`
- Validate code quality

**Expected output:**
```
BUILD SUCCESSFUL in Xs
```

### 3. Build Without Tests (faster)

If you want to build without running tests:

```bash
./gradlew clean build -x test
```

---

## Running the Service

### Using Gradle (Development)

```bash
./gradlew bootRun
```

The service will start on **http://localhost:8080**

### Stop the Service

Press `Ctrl + C` in the terminal where the service is running.

---

## Testing

### Run All Tests

```bash
./gradlew test
```

**Output includes:**
- Unit tests for all validation rules
- Service layer tests
- Integration tests
- Test summary with pass/fail counts

### Run Specific Test Class

```bash
./gradlew test --tests StudentAgeRuleTest
./gradlew test --tests ApplicationValidationServiceTest
```

### Run Specific Test Method

```bash
./gradlew test --tests "StudentAgeRuleTest.studentAgeIsAboveMinimumAge"
```

### View Test Reports

After running tests, open the HTML report:

```bash
open build/reports/tests/test/index.html
```

Or navigate to: `build/reports/tests/test/index.html`

### Test Coverage

The project includes comprehensive tests:

- **Rule Tests** - Each validation rule has 8-12 unit tests
- **Service Tests** - 13 unit tests for ApplicationValidationService
- **Utility Tests** - SSN validator tests

**Coverage:** ~95% line coverage on core validation logic

---

## API Documentation

### Base URL

```
http://localhost:8080/api/v1/applications
```

### Endpoints

#### 1. Validate Application

**POST** `/api/v1/applications/validate`

Validates a FAFSA application against all configured rules.

**Request Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "studentInfo": {
    "firstName": "Jane",
    "lastName": "Smith",
    "ssn": "123456789",
    "dateOfBirth": "2003-05-15"
  },
  "dependencyStatus": "dependent",
  "maritalStatus": "single",
  "household": {
    "numberInHousehold": 4,
    "numberInCollege": 1
  },
  "income": {
    "studentIncome": 5000,
    "parentIncome": 65000
  },
  "stateOfResidence": "CA"
}
```

**Response (Valid Application):**
```json
{
  "status": "VALID",
  "passedRules": [
    {
      "ruleName": "STUDENT_AGE"
    },
    {
      "ruleName": "SSN_FORMAT"
    },
    {
      "ruleName": "DEPENDENT_PARENT_INCOME"
    },
    {
      "ruleName": "INCOME_VALUE"
    },
    {
      "ruleName": "HOUSEHOLD_COLLEGE_COUNT"
    },
    {
      "ruleName": "STATE_CODE"
    },
    {
      "ruleName": "MARRIED_SPOUSE_INFO"
    }
  ],
  "failedRules": []
}
```

**Response (Invalid Application):**
```json
{
  "status": "INVALID",
  "passedRules": [],
  "failedRules": [
    {
      "ruleName": "STUDENT_AGE",
      "message": "Student must be at least 14 years old. Current age: 11"
    },
    {
      "ruleName": "SSN_FORMAT",
      "message": "SSN must be exactly 9 digits. Provided: invalid"
    },
    {
      "ruleName": "DEPENDENT_PARENT_INCOME",
      "message": "Dependent student must have parent income"
    }
  ]
}
```

#### 2. Health Check

**GET** `/api/v1/applications/health`

Simple health check endpoint.

**Response:**
```
Application Validation Service is running
```

### Example curl Commands

**Valid Application:**
```bash
curl -X POST http://localhost:8080/api/v1/applications/validate \
  -H "Content-Type: application/json" \
  -d '{
    "studentInfo": {
      "firstName": "Jane",
      "lastName": "Smith",
      "ssn": "123-45-6789",
      "dateOfBirth": "2003-05-15"
    },
    "dependencyStatus": "dependent",
    "maritalStatus": "single",
    "household": {
      "numberInHousehold": 4,
      "numberInCollege": 1
    },
    "income": {
      "studentIncome": 5000,
      "parentIncome": 65000
    },
    "stateOfResidence": "CA"
  }'
```

**Invalid Application:**
```bash
curl -X POST http://localhost:8080/api/v1/applications/validate \
  -H "Content-Type: application/json" \
  -d '{
    "studentInfo": {
      "firstName": "John",
      "lastName": "Doe",
      "ssn": "invalid",
      "dateOfBirth": "2015-01-01"
    },
    "dependencyStatus": "dependent",
    "maritalStatus": "married",
    "household": {
      "numberInHousehold": 2,
      "numberInCollege": 5
    },
    "income": {
      "studentIncome": -1000
    },
    "stateOfResidence": "XX"
  }'
```

---

### Rule Execution Flow

1. **Request received** by `ApplicationValidationController`
2. **Deserialize JSON** to `Application` object (with case-insensitive enums)
3. **Service collects all rules** via Spring dependency injection
4. **Execute each active rule** against the application
5. **Accumulate results** (passed rules and failed rules)
6. **Determine overall status** (VALID if no failures, INVALID otherwise)
7. **Return results** as JSON (excluding null fields)

---
