# Quantity Measurement Application

A Java-based Quantity Measurement application that evolves from basic measurement equality checks to a Spring Boot based REST service with JPA persistence, validation, API documentation, and monitoring.

This repository represents the **UC17** stage of the project while preserving the learning path and design progression from the earlier use cases.

## Table of Contents

- [Overview](#overview)
- [Use Case Journey](#use-case-journey)
- [Tech Stack](#tech-stack)
- [Supported Measurement Categories](#supported-measurement-categories)
- [Project Structure](#project-structure)
- [Available APIs](#available-apis)
- [Sample Requests](#sample-requests)
- [Example Response](#example-response)
- [Running the Application](#running-the-application)
- [H2 Console Login](#h2-console-login)
- [Testing](#testing)
- [Key Design Principles](#key-design-principles)
- [Key UC17 Highlights](#key-uc17-highlights)
- [Interview Summary](#interview-summary)

## Overview

The Quantity Measurement Application started as a simple object-oriented exercise for comparing feet values and gradually evolved into a multi-category measurement system.

By UC17, the project supports:

- quantity comparison
- unit conversion
- arithmetic operations
- measurement history persistence
- errored history tracking
- REST APIs with JSON/XML support
- validation and centralized exception handling
- H2 database integration
- Swagger/OpenAPI documentation
- Actuator health and metrics
- Spring Security foundation

## Use Case Journey

### UC1 - Feet Measurement Equality

Introduced a simple `Feet` value object with proper equality logic and safe floating-point comparison.

### UC2 - Feet and Inches Measurement Equality

Added inches as a second unit and exposed duplication between separate but similar classes.

### UC3 - Generic Length Quantity

Refactored feet and inches into a common quantity representation backed by a length unit enum.

### UC4 - Extended Length Units

Added yards and centimeters to validate extensibility without changing the main quantity logic.

### UC5 - Unit-to-Unit Conversion

Added conversion support using normalization through a base unit.

### UC6 - Addition of Length Quantities

Added arithmetic between compatible length quantities with result reuse through conversion logic.

### UC7 - Addition with Explicit Target Unit

Allowed callers to specify the desired result unit explicitly.

### UC8 - Unit Responsibility Refactor

Moved conversion behavior into the unit enum to improve separation of concerns and reduce class coupling.

### UC9 - Weight Measurement Category

Added weight-based quantity handling to prove that the design could support more than one measurement category.

### UC10 - Generic Quantity Abstraction

Introduced a more generic measurement design so multiple categories could share the same conceptual structure.

### UC11 - Volume Measurement Category

Added volume units to validate architectural scalability across categories.

### UC12 - UC16

The project continued evolving toward stronger architecture, broader measurement support, better reuse, and persistence-oriented design, preparing it for framework-based integration.

### UC17 - Spring Framework Integration

The current stage migrates the application into a Spring Boot ecosystem with:

- Spring Boot application setup
- REST controllers
- service layer
- Spring Data JPA repository
- JPA entity persistence
- validation
- global exception handling
- H2 database support
- Swagger/OpenAPI documentation
- Actuator endpoints
- Spring Security base configuration
- controller, integration, and repository tests

## Tech Stack

- Java 17
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring Security
- Spring Boot Actuator
- H2 Database
- MySQL driver for production profile
- springdoc OpenAPI / Swagger UI
- JUnit 5
- MockMvc

## Supported Measurement Categories

### Length

- FEET
- INCHES
- YARD
- CENTIMETER

### Weight

- KILOGRAM
- GRAM
- TONNE

### Volume

- LITER
- MILLILITER
- GALLON

### Temperature

- CELSIUS
- FAHRENHEIT
- KELVIN

## Project Structure

```text
src/
|-- main/java/com/app/quantitymeasurement/
|   |-- config/
|   |-- controller/
|   |-- dto/
|   |-- exception/
|   |-- model/
|   |-- repository/
|   |-- service/
|   `-- QuantityMeasurementApplication.java
|-- main/resources/
|   |-- application.properties
|   `-- application-prod.properties
`-- test/java/com/app/quantitymeasurement/
    |-- QuantityMeasurementApplicationTests.java
    |-- QuantityMeasurementControllerTest.java
    `-- QuantityMeasurementRepositoryTest.java
```

## Available APIs

Base path:

```text
/api/v1/quantities
```

### GET APIs

- `GET /`
- `GET /api`
- `GET /api/v1/quantities`
- `GET /api/v1/quantities/history/operation/{operation}`
- `GET /api/v1/quantities/history/type/{measurementType}`
- `GET /api/v1/quantities/history/errored`
- `GET /api/v1/quantities/count/{operation}`
- `GET /actuator/health`
- `GET /actuator/info`
- `GET /actuator/metrics`
- `GET /swagger-ui.html`
- `GET /api-docs`
- `GET /h2-console`

### POST APIs

- `POST /api/v1/quantities/compare`
- `POST /api/v1/quantities/convert`
- `POST /api/v1/quantities/add`
- `POST /api/v1/quantities/subtract`
- `POST /api/v1/quantities/multiply`
- `POST /api/v1/quantities/divide`

## Sample Requests

### Compare

```json
{
  "thisQuantityDTO": {
    "value": 1.0,
    "unit": "FEET",
    "measurementType": "LengthUnit"
  },
  "thatQuantityDTO": {
    "value": 12.0,
    "unit": "INCHES",
    "measurementType": "LengthUnit"
  }
}
```

### Convert

```json
{
  "thisQuantityDTO": {
    "value": 1.0,
    "unit": "FEET",
    "measurementType": "LengthUnit"
  },
  "thatQuantityDTO": {
    "value": 0.0,
    "unit": "INCHES",
    "measurementType": "LengthUnit"
  }
}
```

### Add

```json
{
  "thisQuantityDTO": {
    "value": 1.0,
    "unit": "FEET",
    "measurementType": "LengthUnit"
  },
  "thatQuantityDTO": {
    "value": 12.0,
    "unit": "INCHES",
    "measurementType": "LengthUnit"
  }
}
```

## Example Response

```json
{
  "thisValue": 1.0,
  "thisUnit": "FEET",
  "thisMeasurementType": "LengthUnit",
  "thatValue": 0.0,
  "thatUnit": "INCHES",
  "thatMeasurementType": "LengthUnit",
  "operation": "convert",
  "resultString": null,
  "resultValue": 12.0,
  "resultUnit": "INCHES",
  "resultMeasurementType": "LengthUnit",
  "errorMessage": null,
  "error": false
}
```

## Running the Application

From the project root:

```bash
mvn spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

Useful URLs:

- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- Health: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
- API Base: [http://localhost:8080/api/v1/quantities](http://localhost:8080/api/v1/quantities)

## H2 Console Login

Use these values in H2 Console:

- JDBC URL: `jdbc:h2:mem:quantitymeasurementdb`
- Username: `sa`
- Password: leave blank

## Testing

Run all tests:

```bash
mvn test
```

Current automated coverage includes:

- controller tests with MockMvc
- integration tests with Spring Boot and TestRestTemplate
- repository tests with Data JPA
- OpenAPI, XML, Actuator, and persisted error-history checks

## Key Design Principles

- DRY: repeated measurement logic was progressively generalized
- SRP: controller, service, persistence, and DTO responsibilities are separated
- OCP: new operations and categories are easier to extend without rewriting the whole app
- Encapsulation: data and behavior are organized into focused classes
- Validation-first API design: invalid input is rejected before business processing
- Persistence separation: REST layer does not directly manage database access

## Key UC17 Highlights

- migrated to Spring Boot architecture
- REST-first API design
- JPA-based persistence instead of manual JDBC code
- validation on request DTOs
- centralized exception handling
- persisted history for success and failure cases
- Swagger/OpenAPI support
- H2 console for development verification
- Actuator monitoring endpoints
- security foundation ready for future enhancements

## Interview Summary

If you need to explain this project briefly:

> This is a Quantity Measurement application that evolved through multiple use cases from basic object equality and unit conversion into a Spring Boot based REST service. In UC17, quantity operations such as compare, convert, add, subtract, multiply, and divide are exposed through REST APIs. Input is validated using DTOs, business logic is handled in the service layer, history is stored using Spring Data JPA in an H2 database, and errors are handled centrally using `@ControllerAdvice`. Swagger UI and Actuator are included for documentation and monitoring.
