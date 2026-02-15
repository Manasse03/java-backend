# CLAUDE.md - AI Assistant Guide for java-backend

## Project Overview

Energy billing calculation API built with Spring Boot. It computes billing amounts for two client types (residential "particulier" and professional "pro") based on energy consumption (electricity and gas) with type-specific tariffs.

**Stateless, in-memory service** — no database, no ORM, no migrations.

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.4**
- **Maven 3.9.5** (via Maven Wrapper)
- **JUnit 5** + **MockMvc** for testing

## Build & Run Commands

```bash
# Build the project
./mvnw clean package

# Run tests only
./mvnw test

# Run the application (serves on port 8084)
./mvnw spring-boot:run

# Build without tests
./mvnw clean package -DskipTests
```

## Project Structure

```
src/main/java/com/exo/javabackend/
├── JavaBackendApplication.java              # Entry point (@SpringBootApplication + CommandLineRunner)
├── application/                             # Use case / service layer
│   └── CalculateBillingUseCase.java         # Core billing logic (@Service)
├── domain/                                  # Domain models (framework-agnostic)
│   ├── model/
│   │   ├── Client.java                      # Abstract base class
│   │   ├── ClientParticulier.java           # Residential client (fixed tariffs)
│   │   ├── ClientPro.java                   # Professional client (tariffs vary by revenue)
│   │   ├── TypeEnergie.java                 # Enum: ELECTRICITE, GAZ
│   │   └── dto/
│   │       └── ClientResponseDTO.java       # Response DTO (Java record, shared)
│   └── exception/
│       └── BadRequestException.java         # Custom domain exception
└── infrastructure/                          # Framework/HTTP layer
    └── controller/
        ├── CalculateBillingController.java   # REST endpoints
        ├── config/
        │   └── GlobalControllerException.java  # @ControllerAdvice exception handler
        └── dto/
            ├── ClientParticularRequestDTO.java  # Request DTO (Java record)
            └── ClientProRequestDTO.java         # Request DTO (Java record)
```

### Test Structure

```
src/test/java/com/exo/javabackend/
├── JavaBackendApplicationTests.java          # Spring context smoke test
├── CalculateBillingUseCaseTests.java         # Unit tests (6 tests, direct instantiation)
└── CalculateBillingUseCaseE2ETests.java      # Integration tests (MockMvc, @SpringBootTest)
```

## Architecture

**Layered / Clean Architecture** with three layers:

1. **domain** — Pure domain models and exceptions. No Spring annotations. The `Client` abstract class defines `getTarif(TypeEnergie)` which subclasses override with their pricing logic.
2. **application** — Use cases (services). Contains `CalculateBillingUseCase` which validates client references (regex: `^EKW\d{8}$`), calculates per-energy billing, and returns DTOs.
3. **infrastructure** — Spring MVC controllers, request DTOs, and exception handling. Translates HTTP requests into domain objects and delegates to the application layer.

## REST API

Base URL: `http://localhost:8084`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/billing/calculate/particular-billing` | Calculate billing for residential client |
| POST | `/billing/calculate/pro-billing` | Calculate billing for professional client |

### Business Rules

- Client references must match pattern `EKW` followed by exactly 8 digits
- **Residential tariffs**: Electricity = 0.121, Gas = 0.115
- **Professional tariffs** (revenue <= 1M): Electricity = 0.118, Gas = 0.113
- **Professional tariffs** (revenue > 1M): Electricity = 0.114, Gas = 0.111

## Code Conventions

- **Language**: Domain terms and variable names use French (e.g., `consommations`, `civilite`, `raisonSociale`, `montantParEnergie`, `sommeTotal`)
- **DTOs**: Java records for immutability
- **Domain models**: Traditional classes with inheritance (`Client` -> `ClientParticulier` / `ClientPro`)
- **Polymorphism**: Tariff logic lives in each `Client` subclass via `getTarif()`
- **Response DTO**: Single `ClientResponseDTO` with `Optional<>` fields to handle both client types
- **Package naming**: Lowercase, no underscores
- **Class naming**: PascalCase
- **Method/variable naming**: camelCase
- **Use case pattern**: Service classes named `*UseCase` with a `handle()` method
- **Dependency injection**: Constructor injection (with `@Autowired` on the field — mixed style)

## Testing Conventions

- Unit tests: Direct instantiation of `CalculateBillingUseCase`, no mocking framework
- Integration tests: `@SpringBootTest` + `@AutoConfigureMockMvc` with MockMvc
- Test class naming: `*Tests` suffix (not `*Test`)
- Assertions: `org.junit.jupiter.api.Assertions` (JUnit 5 native)
- Test data uses hardcoded values matching known tariff calculations

## Configuration

- `application.properties` at `src/main/resources/`
- Application name: `java-backend`
- Server port: `8084`
- No profiles, no external config, no environment variables

## Key Constraints

- No database — all computation is stateless
- No security/authentication configured
- No API documentation (Swagger/SpringDoc) configured
- No CI/CD pipelines configured
- No code formatting or linting tools configured
- Minimal dependencies — only `spring-boot-starter-web` and `spring-boot-starter-test`
