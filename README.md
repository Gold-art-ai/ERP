# Rwanda Government ERP System - Payroll & Employee Management

A complete Spring Boot backend system for managing employees and processing payroll with automated calculations, deductions, and messaging.

---

## 1. Database Schema & ERD Design

### Entity Relationship Diagram (ERD)
```
+------------------------+         1 : N         +------------------------+
|      employees         |---------------------->|      employments       |
+------------------------+                       +------------------------+
| id (PK)               |                       | id (PK)                |
| first_name            |                       | employee_identifier    |
| last_name             |                       | employee_id (FK)       |
| email (Unique)        |                       | department             |
| district              |                       | position               |
| mobile                |                       | base_salary            |
| date_of_birth         |                       | status (ACTIVE/INACTIVE)|
+------------------------+                       | joining_date           |
          |                                       | institution            |
          | 1 : N                                 +------------------------+
          v                                                    |
+------------------------+                                      | 1 : N
|      payslips         |<-------------------------------------+
+------------------------+
| id (PK)               |
| employee_id (FK)      |
| employment_id (FK)    |
| base_salary           |
| house_allowance       |
| transport_allowance   |
| gross_salary          |
| employee_tax          |
| pansion               |
| medical_insurance     |
| others                |
| total_deductions      |
| net_salary            |
| status (PENDING/PAID) |
| month_year            |
+------------------------+
          |
          | 1 : N
          v
+------------------------+
|   system_messages      |
+------------------------+
| id (PK)               |
| employee_id (FK)      |
| content               |
| month_year            |
| sent_at               |
+------------------------+
```

### Normalized Database Tables

#### employees Table
Stores personal information about employees.

| Column             | Type         | Constraints      | Description |
|--------------------|--------------|-----------------|-------------|
| id                 | BIGINT       | PK, AUTO_INCREMENT | Unique identifier |
| first_name         | VARCHAR(255) | NOT NULL        | Employee's first name |
| last_name          | VARCHAR(255) | NOT NULL        | Employee's last name |
| email              | VARCHAR(255) | NOT NULL, UNIQUE| Employee's email |
| district           | VARCHAR(255) |                 | Employee's district |
| mobile             | VARCHAR(255) |                 | Employee's mobile number |
| date_of_birth      | DATE         |                 | Employee's date of birth |

#### employments Table
Stores professional employment details.

| Column             | Type         | Constraints      | Description |
|--------------------|--------------|-----------------|-------------|
| id                 | BIGINT       | PK, AUTO_INCREMENT | Unique identifier |
| employee_identifier| VARCHAR(255) | NOT NULL, UNIQUE| Custom employee ID (like EMP001) |
| employee_id        | BIGINT       | FK, NOT NULL, UNIQUE | References employees.id |
| department         | VARCHAR(255) |                 | Employee's department |
| position           | VARCHAR(255) |                 | Employee's position |
| base_salary        | DOUBLE       | NOT NULL        | Employee's base salary |
| status             | VARCHAR(20)  | NOT NULL        | Employment status (ACTIVE/INACTIVE) |
| joining_date       | DATE         |                 | Date employee joined |
| institution        | VARCHAR(255) |                 | Institution name |

#### deductions Table
Stores deduction percentage configurations.

| Column             | Type         | Constraints      | Description |
|--------------------|--------------|-----------------|-------------|
| id                 | BIGINT       | PK, AUTO_INCREMENT | Unique identifier |
| name               | VARCHAR(255) | NOT NULL, UNIQUE| Deduction name |
| percentage         | DOUBLE       | NOT NULL        | Deduction percentage |

#### payslips Table
Stores generated payslips.

| Column             | Type         | Constraints      | Description |
|--------------------|--------------|-----------------|-------------|
| id                 | BIGINT       | PK, AUTO_INCREMENT | Unique identifier |
| employee_id        | BIGINT       | FK, NOT NULL    | References employees.id |
| employment_id      | BIGINT       | FK, NOT NULL    | References employments.id |
| base_salary        | DECIMAL(38,2)| NOT NULL        | Base salary for calculation |
| house_allowance    | DECIMAL(38,2)|                 | Calculated house allowance |
| transport_allowance| DECIMAL(38,2)|                 | Calculated transport allowance |
| gross_salary       | DECIMAL(38,2)|                 | Calculated gross salary |
| employee_tax       | DECIMAL(38,2)|                 | Calculated tax deduction |
| pansion            | DECIMAL(38,2)|                 | Calculated pension deduction |
| medical_insurance  | DECIMAL(38,2)|                 | Calculated medical insurance |
| others             | DECIMAL(38,2)|                 | Calculated other deductions |
| total_deductions   | DECIMAL(38,2)|                 | Sum of all deductions |
| net_salary         | DECIMAL(38,2)|                 | Final take-home salary |
| status             | VARCHAR(20)  | NOT NULL        | Payslip status (PENDING/PAID) |
| month_year         | VARBINARY(255)| NOT NULL       | Month and year of the payslip |

#### system_messages Table
Stores messages sent to employees when payroll is approved.

| Column             | Type         | Constraints      | Description |
|--------------------|--------------|-----------------|-------------|
| id                 | BIGINT       | PK, AUTO_INCREMENT | Unique identifier |
| employee_id        | BIGINT       | FK, NOT NULL    | References employees.id |
| content            | VARCHAR(1000)| NOT NULL        | Message text |
| month_year         | VARBINARY(255)| NOT NULL       | Month and year of the payroll |
| sent_at            | TIMESTAMP(6) | NOT NULL        | Timestamp when message was sent |

---

## 2. Spring Boot Architecture Flow

### Clean Architecture Pattern
This system follows the **Controller-Service-Repository** pattern for clean separation of concerns:

```
┌──────────────────────────────────────────────────────────────────────────────┐
│                        [ Client / Postman ]                                  │
│                                                                               │
│      ┌──────────────────────────┐         HTTP Requests (JSON)               │
│      │                          │◄──────────────────────────────────────────►│
│      │ @RestController          │         (EmployeeController,               │
│      │ (Controllers)            │          EmploymentController,            │
│      │                          │          DeductionController,              │
│      │ Handles API endpoints    │          PayrollController,               │
│      │ & basic validation       │          MessageController)                │
│      └──────────────┬───────────┘                                            │
│                     │                                                        │
│                     │ DTOs (Data Transfer Objects)                           │
│                     │                                                        │
│      ┌──────────────▼───────────┐                                            │
│      │                          │                                            │
│      │    @Service              │                                            │
│      │  (Service Layer)         │ - Payroll calculation logic               │
│      │                          │ - Employee/Employment management          │
│      │ Business Rules & Logic   │ - Deduction management                    │
│      │                          │ - Message generation                       │
│      └──────────────┬───────────┘                                            │
│                     │                                                        │
│                     │ Domain Entities (Employee, Employment, etc.)          │
│                     │                                                        │
│      ┌──────────────▼───────────┐                                            │
│      │                          │                                            │
│      │  @Repository             │                                            │
│      │  (Data Layer)            │ - Spring Data JPA Repositories            │
│      │                          │ - Hibernate ORM                            │
│      └──────────────┬───────────┘                                            │
│                     │                                                        │
│                     │ SQL Queries                                            │
│                     │                                                        │
│      ┌──────────────▼───────────┐                                            │
│      │                          │                                            │
│      │   Database (H2/MySQL)    │ - H2 in-memory (default)                  │
│      │                          │ - Can be configured for MySQL/PostgreSQL   │
│      └──────────────────────────┘                                            │
└──────────────────────────────────────────────────────────────────────────────┘
```

### Layer Explanations

#### 1. Controller Layer
- **File Location**: `src/main/java/com/ERP/ERP/controller/`
- **Responsibilities**:
  - Receive HTTP requests from clients
  - Map request parameters to DTOs
  - Validate basic input
  - Delegate to service layer
  - Return appropriate HTTP responses
- **Controllers**:
  - `EmployeeController`: Employee CRUD operations
  - `EmploymentController`: Employment CRUD operations
  - `DeductionController`: Deduction CRUD operations
  - `PayrollController`: Payroll generation/approval, payslip retrieval
  - `MessageController`: Message retrieval
  - `GlobalExceptionHandler`: Centralized error handling

#### 2. Service Layer
- **File Location**: `src/main/java/com/ERP/ERP/service/`
- **Responsibilities**:
  - Implement business logic
  - Execute payroll calculations
  - Validate business rules
  - Coordinate multiple repositories
  - Handle transaction management
- **Services**:
  - `EmployeeService`: Employee management
  - `EmploymentService`: Employment management
  - `DeductionService`: Deduction management
  - `PayrollService`: Payroll calculation, payslip generation, payroll approval
  - `MessageService`: Message generation and persistence

#### 3. Repository Layer
- **File Location**: `src/main/java/com/ERP/ERP/repository/`
- **Responsibilities**:
  - Database access using Spring Data JPA
  - Custom query methods
  - No business logic here
- **Repositories**:
  - `EmployeeRepository`
  - `EmploymentRepository`
  - `DeductionRepository`
  - `PayslipRepository`
  - `MessageRepository`

#### 4. Model Layer (Entities)
- **File Location**: `src/main/java/com/ERP/ERP/model/`
- **Responsibilities**:
  - Represent database tables as Java objects
  - JPA/Hibernate annotations for ORM mapping
- **Entities**:
  - `Employee`
  - `Employment`
  - `EmploymentStatus` (enum)
  - `Deduction`
  - `Payslip`
  - `PayslipStatus` (enum)
  - `Message`

#### 5. DTO Layer
- **File Location**: `src/main/java/com/ERP/ERP/dto/`
- **Responsibilities**:
  - Transfer data between client and server
  - Separate API contracts from internal entities
- **DTOs**:
  - `EmployeeRequest`: Employee creation/update
  - `EmploymentRequest`: Employment creation/update
  - `PayrollRequest`: Payroll generation/approval (month/year)

#### 6. Configuration Layer
- **File Location**: `src/main/java/com/ERP/ERP/config/`
- **Classes**:
  - `DataInitializer`: Populates default deductions and sample employees on startup
  - `application.properties`: Application configuration

---

## 3. Payroll Calculation Logic

### Calculation Steps

1. **Gross Salary Calculation**
   ```
   Gross Salary = Base Salary + (Base Salary × House%) + (Base Salary × Transport%)
   ```

2. **Total Deductions Calculation**
   ```
   Employee Tax = Base Salary × EmployeeTax%
   Pension = Base Salary × Pansion%
   Medical Insurance = Base Salary × MedicalInsurance%
   Others = Base Salary × Others%
   
   Total Deductions = Employee Tax + Pension + Medical Insurance + Others
   ```

3. **Net Salary Calculation**
   ```
   Net Salary = Base Salary - Total Deductions
   ```

### Example Calculation
For an employee with Base Salary = 70,000 FRW:
- House = 14% → 9,800 FRW
- Transport = 14% → 9,800 FRW
- **Gross Salary = 89,600 FRW**

Deductions:
- EmployeeTax = 30% → 21,000 FRW
- Pansion = 6% → 4,200 FRW
- MedicalInsurance = 5% → 3,500 FRW
- Others = 5% → 3,500 FRW
- **Total Deductions = 32,200 FRW**

- **Net Salary = 70,000 - 32,200 = 37,800 FRW**

---

## 4. API Endpoints

### Base URL
`http://localhost:8080`

### Employee Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/employees` | Create a new employee |
| GET | `/api/employees` | Get all employees |
| GET | `/api/employees/{id}` | Get employee by ID |
| PUT | `/api/employees/{id}` | Update an employee |
| DELETE | `/api/employees/{id}` | Delete an employee |

### Employment Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/employments` | Create a new employment |
| GET | `/api/employments` | Get all employments |
| GET | `/api/employments/active` | Get only active employments |
| GET | `/api/employments/{id}` | Get employment by ID |
| PUT | `/api/employments/{id}` | Update an employment |
| DELETE | `/api/employments/{id}` | Delete an employment |

### Deduction Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/deductions` | Create a new deduction |
| GET | `/api/deductions` | Get all deductions |
| GET | `/api/deductions/{id}` | Get deduction by ID |
| PUT | `/api/deductions/{id}` | Update a deduction |
| DELETE | `/api/deductions/{id}` | Delete a deduction |

### Payroll & Payslip Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/payroll/generate` | Generate payroll for a month/year |
| POST | `/api/payroll/approve` | Approve payroll and generate messages |
| GET | `/api/payroll/payslips` | Get all payslips |
| GET | `/api/payroll/payslips/month` | Get payslips by month/year |
| GET | `/api/payroll/payslips/employee/{id}` | Get payslips by employee ID |
| GET | `/api/payroll/payslips/{id}/download` | Download payslip as PDF |

### Message Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/messages | Get all messages |
| GET | /api/messages/employee/{employeeId} | Get messages for a specific employee |

---

## 5. Configuration & Usage

### Default Deductions (Pre-populated)
| Name | Percentage |
|------|------------|
| EmployeeTax | 30% |
| Pansion | 6% |
| MedicalInsurance | 5% |
| Others | 5% |
| House | 14% |
| Transport | 14% |

### Sample Employees (Pre-populated)
1. **Mugabo Javis**
   - Employee ID: EMP001
   - Department: HR
   - Position: Manager
   - Base Salary: 70,000 FRW
   - Status: ACTIVE
   - Institution: RCA

2. **Michou Michell**
   - Employee ID: EMP002
   - Department: Finance
   - Position: Accountant
   - Base Salary: 35,000 FRW
   - Status: ACTIVE
   - Institution: RCA

### Running the Application
```bash
cd ERP
.\mvnw.cmd spring-boot:run
```

### Swagger UI (API Documentation)
Once the application is running, visit:
`http://localhost:8080/swagger-ui.html`

### H2 Database Console
Access the H2 in-memory database at:
`http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:erpdb`
- Username: `sa`
- Password: (leave blank)

---

## 6. Testing Workflow

1. **Start the application**
2. **Generate payroll** using POST `/api/payroll/generate`
   ```json
   {
     "month": 6,
     "year": 2026
   }
   ```
3. **View payslips** using GET `/api/payroll/payslips`
4. **Download a payslip as PDF** using GET `/api/payroll/payslips/{payslipId}/download` (replace `{payslipId}` with a payslip id from step 3)
5. **Approve payroll** using POST `/api/payroll/approve`
   ```json
   {
     "month": 6,
     "year": 2026
   }
   ```
6. **View generated messages** using GET `/api/messages`

---

## 7. Key Features

✅ **Centralized employee management**<br>
✅ **Automated payroll calculations**<br>
✅ **Deduction management**<br>
✅ **Payslip generation**<br>
✅ **PDF payslip download**<br>
✅ **Message generation on payroll approval**<br>
✅ **Prevents duplicate payroll for same month/year**<br>
✅ **Only processes active employees**<br>
✅ **Proper error handling & clear messages**<br>
✅ **Comprehensive logging**<br>
✅ **Swagger API documentation**<br>
✅ **H2 database console access**<br>

---

## 8. Technologies Used

- **Java 21**
- **Spring Boot 4.x**
- **Spring Data JPA / Hibernate**
- **H2 Database (in-memory)**
- **Lombok**
- **SpringDoc OpenAPI (Swagger)**
- **Maven**

---

## 9. Future Enhancements

- Add user authentication and authorization
- Change to PostgreSQL/MySQL for persistent database
- Add PDF export for payslips
- Add email notifications
- Add department/institution management
- Add payroll history reporting
- Add import/export functionality for employees

---

## Author
Rwanda Government ERP System
"# ERP" 
