# PRESENTATION GUIDE
## Employee Payroll Management System - Team of 3

This guide tells each team member exactly what to present, in what order,
and what to say. Total presentation time: ~15-20 minutes.

---

## TEAM MEMBER ROLES

| Person | Role | Slides/Topics | Time |
|--------|------|---------------|------|
| Person 1 | Introduction + Architecture | Project overview, architecture, folder structure, design principles | 5 min |
| Person 2 | OOP + Code Walkthrough | OOP concepts with code, salary formulas, class diagram | 5 min |
| Person 3 | Live Demo + Conclusion | Run the app live, show all features, viva preparation | 5-10 min |

---

## SLIDE-BY-SLIDE BREAKDOWN

---

### PERSON 1: Introduction + Architecture (5 minutes)

#### Slide 1: Title Slide
```
Employee Payroll Management System
Capstone Project

Team Members:
  - [Name 1]
  - [Name 2]
  - [Name 3]

Technology: Java 17 | Console Application
```

#### Slide 2: Problem Statement
Say this:
"In any company, managing employee salaries manually is error-prone.
 Different employees have different salary rules - Regular employees
 get allowance but pay tax, Contract employees get bonus but no tax,
 and Interns just get a fixed stipend. We built a system that handles
 all of this automatically."

Key points to mention:
- Manual payroll is error-prone
- Different rules for different employee types
- Need for automated calculation and record keeping

#### Slide 3: Project Features
List these 8 features:
1. Add/View/Search/Update/Delete Employees (CRUD)
2. Polymorphic Salary Calculation (no if-else)
3. Professional Payslip Generation
4. Bulk Import from CSV File
5. Export Employee Data to File
6. Payroll Summary Report
7. Input Validation with Custom Exceptions
8. Resilient File Import (skips bad rows)

#### Slide 4: Architecture Diagram
Show this diagram and explain the layers:
```
+--------------------------------------------------------------+
|                     PRESENTATION LAYER                        |
|                        Main.java                              |
|                   (User input and menu)                       |
+--------------------------------------------------------------+
                          |
                          v
+--------------------------------------------------------------+
|                      SERVICE LAYER                            |
|   EmployeeService | PayrollService | FileService             |
|   (Business logic, CRUD, reports, file I/O)                  |
+--------------------------------------------------------------+
                          |
                          v
+--------------------------------------------------------------+
|                       UTILITY LAYER                           |
|         PayrollCalculator  |  ValidationUtil                 |
|         (Formulas only)       (Validation only)              |
+--------------------------------------------------------------+
                          |
                          v
+--------------------------------------------------------------+
|                        MODEL LAYER                            |
|   Employee (abstract) -> Regular | Contract | Intern         |
+--------------------------------------------------------------+
```

Say this:
"Our project follows Layered Architecture. Each layer has a specific job.
 The top layer handles user input, the service layer has business logic,
 utilities do calculations and validation, and the model layer holds data.
 Each layer only talks to the layer below it - never upwards."

#### Slide 5: Folder Structure
Show the folder structure (from README.md) and say:
"We organized our code into packages by responsibility.
 Models are in model/, services in service/, and so on.
 This is Separation of Concerns."

#### Slide 6: Design Principles
Show this table and briefly explain each:

| Principle | How We Used It |
|-----------|---------------|
| Single Responsibility | Each class has ONE job |
| Open/Closed | Add new employee types without changing existing code |
| DRY | Constants in one file, formulas in one calculator |
| Separation of Concerns | Scanner ONLY in Main.java |

---

### PERSON 2: OOP + Code Walkthrough (5 minutes)

#### Slide 7: OOP Concepts Overview
Show the 4 pillars and where each is used:

| Pillar | Where |
|--------|-------|
| Abstraction | Employee class is abstract, has abstract calculateSalary() |
| Encapsulation | Private fields, public getters/setters |
| Inheritance | RegularEmployee extends Employee |
| Polymorphism | calculateSalary() runs different code based on object type |

#### Slide 8: Class Diagram
Show the class diagram from README.md and explain:
"Employee is the abstract parent class with 6 private fields.
 It has an abstract method calculateSalary() that each child must implement.
 RegularEmployee, ContractEmployee, and Intern each override this method
 with their own salary formula."

#### Slide 9: Show Employee.java Code
Open Employee.java and point out:
- Private fields (encapsulation)
- abstract keyword on class and method
- Constructor with Objects.requireNonNull()
- equals() and hashCode() based on ID

Say: "Notice the fields are private - this is encapsulation.
 The calculateSalary() method is abstract - each child class MUST provide
 its own implementation."

#### Slide 10: Salary Formulas
Show all three formulas with examples:

REGULAR EMPLOYEE (Base = 50000, Leave = 2):
```
Allowance = 50000 x 0.20 = 10,000
Gross     = 50000 + 10000 = 60,000
Tax       = 60000 x 0.10 = 6,000
Leave     = (50000/30) x 2 = 3,333.33
Net       = 60,000 - 6,000 - 3,333.33 = Rs.50,666.67
```

CONTRACT EMPLOYEE (Base = 30000, Leave = 1):
```
Bonus = 30000 x 0.10 = 3,000
Gross = 30000 + 3000 = 33,000
Leave = (30000/30) x 1 = 1,000
Net   = 33,000 - 1,000 = Rs.32,000.00
```

INTERN (Base = 12000):
```
Net = 12,000 (no deductions)
```

#### Slide 11: Show PayrollCalculator.java
Open PayrollCalculator.java and point out:
- Each method does ONE calculation (SRP)
- No Scanner, no file operations (clean utility)
- Java 17 switch expression with yield keyword
- How it handles all 3 types without if-else

Say: "This is where the actual salary math happens.
 The switch expression picks the right formula based on employee type.
 yield is a Java 17 keyword that returns a value from a switch block."

#### Slide 12: Polymorphism in Action
Show this code and explain:
```java
// At runtime, Java checks the ACTUAL type of the object
Employee emp1 = new RegularEmployee(101, "John", "IT", "Dev", 50000, 2);
Employee emp2 = new ContractEmployee(102, "Jane", "HR", "Rec", 30000, 1);

emp1.calculateSalary();  // Runs RegularEmployee's version -> 50666.67
emp2.calculateSalary();  // Runs ContractEmployee's version -> 32000.00
```

Say: "Both variables are declared as Employee, but Java knows emp1 is actually
 a RegularEmployee and emp2 is a ContractEmployee. It calls the correct version
 automatically. This is runtime polymorphism."

#### Slide 13: Exception Handling
Show the custom exceptions and how they are used:
```
DuplicateEmployeeException  -> When adding an ID that already exists
EmployeeNotFoundException   -> When searching for an ID that doesn't exist
InvalidSalaryException      -> When salary is negative or zero
InvalidLeaveException       -> When leave is negative or > 30
FileProcessingException     -> When file read/write fails
```

Say: "Instead of generic exceptions, we created custom exceptions that
 give meaningful error messages. The application NEVER crashes - it
 catches all exceptions and shows user-friendly messages."

---

### PERSON 3: Live Demo + Conclusion (5-10 minutes)

#### Slide 14: Live Demo Title
"LIVE DEMONSTRATION"

#### Demo Steps (do these in order):

1. COMPILE the project
   ```
   javac -d out --source 17 src/Main.java src/model/*.java src/service/*.java src/util/*.java src/exception/*.java src/constants/*.java src/enums/*.java
   ```

2. RUN the project
   ```
   java -cp out Main
   ```

3. Choose 7 - BULK IMPORT
   - Show that 5 employees are imported from CSV
   - Point out "Successfully Imported: 5, Skipped: 0"

4. Choose 2 - VIEW ALL EMPLOYEES
   - Show the formatted table with all 5 employees
   - Point out different types: Regular, Contract, Intern

5. Choose 1 - ADD EMPLOYEE
   - Add a new employee manually
   - Show validation in action (try entering invalid data)

6. Choose 3 - SEARCH EMPLOYEE
   - Search by ID 101
   - Show the detailed view with calculated net salary

7. Choose 6 - GENERATE PAYSLIP
   - Generate payslip for ID 101 (Regular Employee)
   - Show the professional payslip with earnings and deductions
   - Type 'y' to export to file
   - Show the generated file in data/payslips/

8. Choose 9 - PAYROLL SUMMARY
   - Show the summary table with all employees
   - Point out total base salary vs total net payout

9. Try ERROR HANDLING
   - Try to add an employee with an existing ID (show DuplicateEmployeeException)
   - Try to search for a non-existent ID (show EmployeeNotFoundException)
   - Type "abc" when asked for a number (show input validation)

10. Choose 10 - EXIT

#### Slide 15: Sequence Diagram
Show the sequence diagram and explain the flow for Generate Payslip:
```
User -> Main -> EmployeeService.searchEmployee(id)
                    Returns Employee object
        Main -> PayrollService.generatePayslip(employee)
                    Internally calls employee.calculateSalary() [polymorphism!]
                    Returns formatted payslip string
        Main -> Display to user
        Main -> FileService.exportPayslip() [if user says yes]
```

#### Slide 16: Technologies Used
| Technology | Purpose |
|-----------|---------|
| Java 17 | Language with modern features (switch expressions) |
| Abstract Classes | Abstraction and polymorphism |
| HashMap | Fast employee storage and lookup |
| BufferedReader/Writer | Efficient file I/O |
| java.nio.file | Modern file API (Path, Files) |
| Enum | Type-safe employee classification |
| try-with-resources | Automatic resource cleanup |
| Custom Exceptions | Meaningful error handling |

#### Slide 17: Future Enhancements
1. Replace HashMap with MySQL database
2. Add Spring Boot REST API
3. Add JavaFX GUI
4. Generate PDF payslips
5. Add unit tests with JUnit 5

#### Slide 18: Thank You
```
Thank You!
Questions?

Team Members:
  - [Name 1]
  - [Name 2]
  - [Name 3]
```

---

## TIPS FOR THE PRESENTATION

### Before the Presentation
1. Make sure Java is installed: `java --version`
2. Compile the project BEFORE the presentation so it's ready
3. Keep the terminal open and ready
4. Have the code files open in your IDE

### During the Presentation
1. DON'T read from slides - explain in your own words
2. When showing code, ZOOM IN so the audience can read it
3. For the live demo, go slowly and explain what you're doing
4. If something goes wrong, don't panic - explain the error

### Handling Questions
- If you don't know the answer: "That's a great question, let me check the code"
- Use the README.md Viva Q&A section to prepare
- The most common questions will be about polymorphism and why HashMap
- Know the salary formulas by heart

---

## HOW TO MAKE SLIDES

Use any of these tools:
- Google Slides (free, easy to share)
- PowerPoint
- Canva (for better designs)

Recommended slide design:
- Dark background with light text (looks professional)
- Use code screenshots from your IDE (better than copy-pasting text)
- Keep text minimal - speak the details
- Use the diagrams from this guide directly in slides
- Total slides: 15-18

---

## FILES TO KEEP OPEN DURING DEMO

1. Terminal (for running the program)
2. Employee.java (to show abstraction)
3. RegularEmployee.java (to show inheritance + polymorphism)
4. PayrollCalculator.java (to show salary formulas)
5. EmployeeService.java (to show HashMap usage)
6. data/employees.txt (to show CSV format)
