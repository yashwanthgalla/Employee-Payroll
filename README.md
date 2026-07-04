# Employee Payroll Management System
# Capstone Project - Presentation Guide

> A production-quality Java 17 console application demonstrating OOP, Collections,
> Exception Handling, File I/O, SOLID Principles, and Layered Architecture.

---

## Project Overview

The Employee Payroll Management System manages employee records, computes salaries
using polymorphic dispatch, generates professional payslips, and supports bulk CSV
import/export through an interactive console menu.

| Feature               | What It Does                                           |
|-----------------------|--------------------------------------------------------|
| Employee CRUD         | Add, view, search, update, and delete employees        |
| Polymorphic Salary    | Runtime dispatch - no if-else for salary logic          |
| Payslip Generation    | Formatted payslips with earnings and deductions         |
| Bulk CSV Import       | Resilient import - skips bad rows, reports reasons      |
| CSV Export            | Save in-memory data back to employees.txt              |
| Payroll Summary       | Aggregated report across all employees                 |
| Custom Exceptions     | Domain-specific error handling with clear messages      |
| File I/O              | java.nio.file with try-with-resources                  |

---

## How to Compile and Run

```
cd PayrollSystem
javac -d out --source 17 src/Main.java src/model/*.java src/service/*.java src/util/*.java src/exception/*.java src/constants/*.java src/enums/*.java
java -cp out Main
```

### REST API Mode

The project now also exposes a Spring Boot REST API for Postman or any HTTP client.

```bash
.\mvnw.cmd spring-boot:run
```

Available endpoints:

- `GET /api/employees` - returns all employees as JSON
- `GET /api/employees/{id}` - returns one employee by ID with salary details
- `POST /api/employees` - adds a new employee and saves to `data/employees.txt`
- `PUT /api/employees/{id}` - updates an employee and saves to `data/employees.txt`
- `DELETE /api/employees/{id}` - deletes an employee and saves to `data/employees.txt`

Example:

```bash
GET http://localhost:8000/api/employees/101
```

Sample POST body:

```json
{
  "id": 106,
  "name": "Ravi Kumar",
  "department": "IT",
  "designation": "Developer",
  "employeeType": "REGULAR",
  "baseSalary": 50000,
  "leaveTaken": 2
}
```

### Quick Demo Steps
1. Choose 7 (Bulk Import) to load sample employees from data/employees.txt
2. Choose 2 (View All) to see the imported records in a table
3. Choose 6 (Generate Payslip) for employee ID 101
4. Choose 9 (Payroll Summary) to see the aggregated report
5. Choose 10 (Exit) to quit

---

## Folder Structure

```
PayrollSystem/
|
|-- src/
|   |-- Main.java                        <-- Entry point (menu + input)
|   |
|   |-- model/
|   |   |-- Employee.java                <-- Abstract parent class
|   |   |-- RegularEmployee.java         <-- Full-time employee
|   |   |-- ContractEmployee.java        <-- Contract employee
|   |   |-- Intern.java                  <-- Intern/Trainee
|   |
|   |-- service/
|   |   |-- EmployeeService.java         <-- CRUD operations (HashMap)
|   |   |-- PayrollService.java          <-- Payslip and summary reports
|   |   |-- FileService.java            <-- CSV file read/write
|   |
|   |-- util/
|   |   |-- PayrollCalculator.java       <-- Salary calculation formulas
|   |   |-- ValidationUtil.java          <-- Input validation rules
|   |
|   |-- exception/
|   |   |-- DuplicateEmployeeException.java
|   |   |-- EmployeeNotFoundException.java
|   |   |-- InvalidSalaryException.java
|   |   |-- InvalidLeaveException.java
|   |   |-- FileProcessingException.java
|   |
|   |-- constants/
|   |   |-- PayrollConstants.java        <-- All fixed values in one place
|   |
|   |-- enums/
|       |-- EmployeeType.java            <-- REGULAR, CONTRACT, INTERN
|
|-- data/
|   |-- employees.txt                    <-- CSV employee data
|   |-- payslips/                        <-- Generated payslip files
|
|-- out/                                 <-- Compiled .class files
|-- README.md
```

---

## Architecture Diagram

```
+--------------------------------------------------------------+
|                     PRESENTATION LAYER                        |
|                        Main.java                              |
|                   (Scanner, Menu, I/O)                        |
+-----------+-----------------+----------------+---------------+
            |                 |                |
            v                 v                v
+------------------+ +----------------+ +--------------------+
| EmployeeService  | | PayrollService | |    FileService     |
|  (CRUD, HashMap) | | (Payslip, Rpt) | |  (CSV I/O, Export) |
+--------+---------+ +-------+--------+ +---------+----------+
         |                   |                     |
         v                   v                     v
+--------------------------------------------------------------+
|                       UTILITY LAYER                           |
|            PayrollCalculator  .  ValidationUtil               |
+-----------------------------+--------------------------------+
                              |
                              v
+--------------------------------------------------------------+
|                        MODEL LAYER                            |
|    Employee (abstract)                                        |
|      |-- RegularEmployee                                     |
|      |-- ContractEmployee                                    |
|      |-- Intern                                              |
+--------------------------------------------------------------+
                              |
                              v
+--------------------------------------------------------------+
|                   CROSS-CUTTING CONCERNS                      |
|   constants/  .  enums/  .  exception/                        |
+--------------------------------------------------------------+
```

### How Data Flows (Top to Bottom)
1. User types input in Main.java
2. Main.java calls EmployeeService / PayrollService / FileService
3. Services use PayrollCalculator and ValidationUtil for logic
4. Services operate on Employee model objects
5. Constants, Enums, and Exceptions are used across all layers

---

## Class Diagram

```
+------------------------------+
|     <<abstract>>             |
|       Employee               |
+------------------------------+
| - id: int                    |
| - name: String               |
| - department: String         |
| - designation: String        |
| - baseSalary: double         |
| - leaveTaken: int            |
+------------------------------+
| + calculateSalary(): double  |  <-- abstract
| + getEmployeeType(): Type    |  <-- abstract
| + getters / setters          |
| + toString() / equals()      |
+----------+-------------------+
           |
    +------+----------+
    |      |          |
    v      v          v
+--------+ +--------+ +--------+
|Regular | |Contract| | Intern |
|Employee| |Employee| |        |
+--------+ +--------+ +--------+
|override| |override| |override|
|salary()| |salary()| |salary()|
+--------+ +--------+ +--------+


+-----------------+   +------------------+   +------------------+
| EmployeeService |   |  PayrollService  |   |   FileService    |
+-----------------+   +------------------+   +------------------+
| HashMap<ID,Emp> |   | generatePayslip()|   | loadFromFile()   |
| addEmployee()   |   | payrollSummary() |   | saveEmployees()  |
| updateEmployee()|   +------------------+   | exportPayslip()  |
| deleteEmployee()|                          +------------------+
| searchEmployee()|
| viewAll()       |
+-----------------+

+------------------+   +------------------+
| PayrollCalculator|   |  ValidationUtil  |
+------------------+   +------------------+
| calculateTax()   |   | validateSalary() |
| calcAllowance()  |   | validateId()     |
| calcBonus()      |   | validateName()   |
| calcLeaveDed()   |   | validateLeave()  |
| calcNetSalary()  |   | validateBlank()  |
+------------------+   +------------------+
```

---

## Sequence Diagram - Generate Payslip

```
User          Main           EmployeeService    PayrollService    FileService
 |              |                  |                  |                |
 |-- choose 6 ->|                  |                  |                |
 |              |-- searchEmp(id)->|                  |                |
 |              |<-- Employee -----|                  |                |
 |              |-- generatePayslip(emp) ----------->|                |
 |              |                  |  emp.calcSalary()|                |
 |              |                  |  PayrollCalc <---|                |
 |              |<-- payslip str ----------------------|                |
 |<- display ---|                  |                  |                |
 |-- export? y->|                  |                  |                |
 |              |-- exportPayslip(emp, str) ------------------------->|
 |              |                  |                  |   write file   |
 |              |<-- success -------------------------------------------
 |<- confirm ---|                  |                  |                |
```

---

## ER Diagram

```
+----------------------------------------+
|              EMPLOYEE                  |
+----------------------------------------+
| PK  id           INT                  |
|     name         VARCHAR(100)          |
|     department   VARCHAR(50)           |
|     designation  VARCHAR(50)           |
|     type         ENUM(REGULAR,         |
|                       CONTRACT,        |
|                       INTERN)          |
|     baseSalary   DECIMAL(12,2)         |
|     leaveTaken   INT                   |
+----------------------------------------+
| Constraints:                           |
|   id > 0  (UNIQUE, NOT NULL)           |
|   name matches [a-zA-Z ]+             |
|   baseSalary > 0                       |
|   leaveTaken >= 0, <= 30               |
+----------------------------------------+
         |
         | 1 ---- * (generated)
         v
+----------------------------------------+
|              PAYSLIP                   |
+----------------------------------------+
|     employeeId   INT (FK -> Employee)  |
|     date         DATE                  |
|     grossSalary  DECIMAL(12,2)         |
|     tax          DECIMAL(12,2)         |
|     deductions   DECIMAL(12,2)         |
|     netSalary    DECIMAL(12,2)         |
+----------------------------------------+
```

---

## Salary Rules (Important for Viva/QA)

### Regular Employee
```
Gross  = Base Salary + 20% Allowance
Tax    = 10% of Gross
Leave  = (Base / 30) x Days Taken
Net    = Gross - Tax - Leave Deduction

Example: Base = 50000, Leave = 2
  Allowance = 50000 x 0.20 = 10000
  Gross     = 50000 + 10000 = 60000
  Tax       = 60000 x 0.10 = 6000
  Leave     = (50000/30) x 2 = 3333.33
  Net       = 60000 - 6000 - 3333.33 = 50666.67
```

### Contract Employee
```
Gross  = Base Salary + 10% Bonus
Tax    = 0 (exempt)
Leave  = (Base / 30) x Days Taken
Net    = Gross - Leave Deduction

Example: Base = 30000, Leave = 1
  Bonus = 30000 x 0.10 = 3000
  Gross = 30000 + 3000 = 33000
  Leave = (30000/30) x 1 = 1000
  Net   = 33000 - 1000 = 32000
```

### Intern
```
Net = Base Salary (stipend as-is)
No tax, no leave deduction

Example: Base = 12000
  Net = 12000
```

---

## OOP Concepts Used (For Presentation)

### 1. Abstraction
- Employee class is abstract - you cannot create `new Employee()`
- It has an abstract method `calculateSalary()` that each child must implement
- File: Employee.java

### 2. Encapsulation
- All fields in Employee are private
- Access only through getters and setters
- Data is protected from direct modification

### 3. Inheritance
- RegularEmployee, ContractEmployee, Intern all extend Employee
- They inherit all fields and methods from the parent
- They use `super()` to call the parent constructor

### 4. Polymorphism
- Each subclass overrides `calculateSalary()` differently
- When we call `employee.calculateSalary()`, Java decides AT RUNTIME which version to run
- No if-else needed! The JVM picks the right method automatically
- This is the most important concept to explain

### Example of Polymorphism in action:
```java
Employee emp = new RegularEmployee(...);   // Runtime type = RegularEmployee
emp.calculateSalary();                      // Calls RegularEmployee's version

Employee emp2 = new ContractEmployee(...); // Runtime type = ContractEmployee
emp2.calculateSalary();                     // Calls ContractEmployee's version
```

---

## Design Principles Used

### SOLID Principles

| Principle | Where We Used It |
|-----------|-----------------|
| S - Single Responsibility | Each class has ONE job. PayrollCalculator only calculates. ValidationUtil only validates. |
| O - Open/Closed | To add a new employee type, just create a new subclass. No existing code changes. |
| L - Liskov Substitution | Any Employee subclass can be used wherever Employee is expected. |
| I - Interface Segregation | Services have focused methods. No god-class doing everything. |
| D - Dependency Inversion | Main depends on services, services depend on models. Never the reverse. |

### Other Principles

| Principle | How We Applied It |
|-----------|------------------|
| DRY (Don't Repeat Yourself) | All constants in one file. Salary formulas in one calculator. |
| KISS (Keep It Simple) | Each method does one thing. No complex nested logic. |
| Separation of Concerns | Scanner ONLY in Main.java. Services never read input. |
| Clean Architecture | Layered: Presentation -> Service -> Utility -> Model |

---

## Java Features Used

| Feature | Where | Why |
|---------|-------|-----|
| Abstract Class | Employee.java | Forces subclasses to implement calculateSalary() |
| Enum | EmployeeType.java | Restricts employee types to 3 valid values |
| HashMap | EmployeeService | Fast O(1) lookup by employee ID |
| ArrayList | viewAllEmployees() | Ordered list for display |
| BufferedReader/Writer | FileService | Efficient file I/O |
| java.nio.file (Path, Files) | FileService | Modern file API (better than old File class) |
| LocalDate | PayrollService | Current date for payslips |
| StringBuilder | PayrollService | Efficient string building |
| Switch Expression (Java 17) | PayrollCalculator, Main | Cleaner than if-else chains |
| try-with-resources | FileService | Auto-closes files even if error occurs |
| Objects.requireNonNull() | Employee, Services | Null safety checks |
| Custom Exceptions | exception/ package | Meaningful error messages |
| Collections.unmodifiable | EmployeeService | Prevents outside code from modifying data |

---

## Common Viva Questions and Answers

### Q: Why is Employee abstract?
A: Because we never want to create a plain "Employee" object. Every employee must be one of the 3 types (Regular, Contract, Intern). Making it abstract forces us to use the subclasses.

### Q: How does polymorphism work here?
A: When we call `employee.calculateSalary()`, Java checks the ACTUAL object type at runtime. If it's a RegularEmployee, it runs RegularEmployee's version. If it's a ContractEmployee, it runs that version. We don't need any if-else to decide.

### Q: Why use HashMap instead of ArrayList?
A: HashMap gives O(1) lookup by key (employee ID). With ArrayList, we would need to loop through every element to find an employee - that's O(n). HashMap is much faster for search operations.

### Q: Why is Scanner only in Main.java?
A: This is Separation of Concerns. Services should only contain business logic. If services read input directly, they become tightly coupled to the console and can't be reused with a GUI or web interface.

### Q: What happens if the CSV file has a bad row?
A: The import skips that row, records the reason, and continues with the next row. At the end, it prints how many were imported and how many were skipped. The application never crashes.

### Q: Why custom exceptions instead of generic Exception?
A: Custom exceptions are more descriptive. `DuplicateEmployeeException` immediately tells you what went wrong. A generic `Exception("error")` doesn't give you that clarity.

### Q: What is try-with-resources?
A: It automatically closes resources (files, connections) after use, even if an exception occurs. Without it, we'd need try-catch-finally blocks and manual close() calls.

### Q: How would you add a new employee type (e.g., PartTimeEmployee)?
A: Just create a new class that extends Employee, override calculateSalary(), add PART_TIME to the enum. No existing code needs to change. This is the Open/Closed Principle.

---

## Future Enhancements (For Presentation Conclusion)

| # | Enhancement | Description |
|---|-------------|-------------|
| 1 | Database | Replace HashMap with MySQL/PostgreSQL via JDBC |
| 2 | REST API | Expose endpoints via Spring Boot |
| 3 | GUI | Add JavaFX or web-based frontend |
| 4 | PDF Payslips | Generate payslips as PDF using iText library |
| 5 | Email | Auto-email payslips to employees |
| 6 | Unit Tests | Add JUnit 5 test suite |
| 7 | Authentication | Role-based access (Admin vs Employee) |
| 8 | Leave Management | Apply, approve, track leave balances |

---

## License

This project is developed for educational and capstone demonstration purposes.

Built with Java 17 | Layered Architecture | SOLID Principles
