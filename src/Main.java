import constants.PayrollConstants;
import enums.EmployeeType;
import exception.*;
import model.ContractEmployee;
import model.Employee;
import model.Intern;
import model.RegularEmployee;
import service.EmployeeService;
import service.FileService;
import service.PayrollService;
import util.ValidationUtil;

import java.util.List;
import java.util.Scanner;

/*
 * Main.java - Entry Point
 * -----------------------
 * This is where the application starts. It shows a menu and takes user input.
 *
 * IMPORTANT DESIGN RULE:
 * Scanner (user input) is ONLY used in this file.
 * No service class reads input directly - they receive data as parameters.
 * This is called "Separation of Concerns".
 *
 * Flow: User → Main (input) → Service (logic) → Model (data)
 */
public class Main {

    // Create one instance of each service (used throughout the app)
    private static final EmployeeService employeeService = new EmployeeService();
    private static final PayrollService  payrollService  = new PayrollService();
    private static final FileService     fileService     = new FileService();
    private static final Scanner         scanner         = new Scanner(System.in);

    // ========================= MAIN METHOD =========================
    public static void main(String[] args) {
        System.out.println("\n  Welcome to the Employee Payroll Management System!");
        System.out.println("  Built with Java 17\n");

        boolean running = true;

        // Infinite loop - keeps showing menu until user picks Exit
        while (running) {
            displayMenu();
            int choice = readIntInput("  Enter your choice: ");

            // Java 17 switch expression - cleaner than if-else chain
            switch (choice) {
                case 1  -> addEmployee();
                case 2  -> viewAllEmployees();
                case 3  -> searchEmployee();
                case 4  -> updateEmployee();
                case 5  -> deleteEmployee();
                case 6  -> generatePayslip();
                case 7  -> bulkImportEmployees();
                case 8  -> exportEmployeeData();
                case 9  -> displayPayrollSummary();
                case 10 -> {
                    running = false;
                    System.out.println("\n  Goodbye! Thank you for using the Payroll System.\n");
                }
                default -> System.out.println("\n  Invalid choice. Please select 1-10.\n");
            }
        }

        scanner.close();
    }

    // ========================= MENU DISPLAY =========================
    private static void displayMenu() {
        System.out.println(PayrollConstants.DOUBLE_RULE);
        System.out.println("        EMPLOYEE PAYROLL MANAGEMENT SYSTEM");
        System.out.println(PayrollConstants.DOUBLE_RULE);
        System.out.println("   1 | Add Employee");
        System.out.println("   2 | View All Employees");
        System.out.println("   3 | Search Employee");
        System.out.println("   4 | Update Employee");
        System.out.println("   5 | Delete Employee");
        System.out.println("   6 | Generate Payslip");
        System.out.println("   7 | Bulk Import Employees");
        System.out.println("   8 | Export Employee Data");
        System.out.println("   9 | Display Payroll Summary");
        System.out.println("  10 | Exit");
        System.out.println(PayrollConstants.HORIZONTAL_RULE);
    }

    // ========================= 1. ADD EMPLOYEE =========================
    private static void addEmployee() {
        System.out.println("\n" + PayrollConstants.HORIZONTAL_RULE);
        System.out.println("  ADD NEW EMPLOYEE");
        System.out.println(PayrollConstants.HORIZONTAL_RULE);

        try {
            int id = readIntInput("  Enter Employee ID   : ");
            ValidationUtil.validatePositiveId(id);

            String name = readStringInput("  Enter Name          : ");
            ValidationUtil.validateName(name);

            String department = readStringInput("  Enter Department    : ");
            ValidationUtil.validateNonBlank(department, "Department");

            String designation = readStringInput("  Enter Designation   : ");
            ValidationUtil.validateNonBlank(designation, "Designation");

            System.out.println("  Select Employee Type:");
            System.out.println("    1 - Regular");
            System.out.println("    2 - Contract");
            System.out.println("    3 - Intern");
            int typeChoice = readIntInput("  Enter type (1/2/3)  : ");

            EmployeeType type = switch (typeChoice) {
                case 1  -> EmployeeType.REGULAR;
                case 2  -> EmployeeType.CONTRACT;
                case 3  -> EmployeeType.INTERN;
                default -> throw new IllegalArgumentException("Invalid type. Choose 1, 2, or 3.");
            };

            double salary = readDoubleInput("  Enter Base Salary   : ");
            ValidationUtil.validatePositiveSalary(salary);

            int leave = readIntInput("  Enter Leave Taken   : ");
            ValidationUtil.validateLeave(leave);

            // Create the right subclass based on type (Factory pattern)
            Employee employee = switch (type) {
                case REGULAR  -> new RegularEmployee(id, name, department, designation, salary, leave);
                case CONTRACT -> new ContractEmployee(id, name, department, designation, salary, leave);
                case INTERN   -> new Intern(id, name, department, designation, salary, leave);
            };

            employeeService.addEmployee(employee);
            System.out.println("\n  Employee '" + name + "' (ID: " + id + ") added successfully.\n");

        } catch (DuplicateEmployeeException | InvalidSalaryException | InvalidLeaveException ex) {
            System.out.println("\n  Error: " + ex.getMessage() + "\n");
        } catch (IllegalArgumentException ex) {
            System.out.println("\n  Validation Error: " + ex.getMessage() + "\n");
        }
    }

    // ========================= 2. VIEW ALL EMPLOYEES =========================
    private static void viewAllEmployees() {
        List<Employee> employees = employeeService.viewAllEmployees();

        if (employees.isEmpty()) {
            System.out.println("\n  No employees found in the system.\n");
            return;
        }

        System.out.println("\n" + PayrollConstants.HORIZONTAL_RULE);
        System.out.printf(PayrollConstants.TABLE_HEADER_FORMAT,
                "ID", "Name", "Type", "Department", "Designation", "Base Salary");
        System.out.println(PayrollConstants.HORIZONTAL_RULE);

        for (Employee emp : employees) {
            System.out.printf(PayrollConstants.TABLE_ROW_FORMAT,
                    emp.getId(), emp.getName(),
                    emp.getEmployeeType().getDisplayName(),
                    emp.getDepartment(), emp.getDesignation(),
                    emp.getBaseSalary());
        }

        System.out.println(PayrollConstants.HORIZONTAL_RULE);
        System.out.println("  Total Employees: " + employees.size() + "\n");
    }

    // ========================= 3. SEARCH EMPLOYEE =========================
    private static void searchEmployee() {
        System.out.println("\n" + PayrollConstants.HORIZONTAL_RULE);
        System.out.println("  SEARCH EMPLOYEE");
        System.out.println(PayrollConstants.HORIZONTAL_RULE);

        try {
            int id = readIntInput("  Enter Employee ID to search: ");
            Employee emp = employeeService.searchEmployee(id);

            System.out.println("\n  " + PayrollConstants.HORIZONTAL_RULE);
            System.out.println("  EMPLOYEE DETAILS");
            System.out.println("  " + PayrollConstants.HORIZONTAL_RULE);
            System.out.println("  ID          : " + emp.getId());
            System.out.println("  Name        : " + emp.getName());
            System.out.println("  Department  : " + emp.getDepartment());
            System.out.println("  Designation : " + emp.getDesignation());
            System.out.println("  Type        : " + emp.getEmployeeType().getDisplayName());
            System.out.printf("  Base Salary : Rs.%,.2f%n", emp.getBaseSalary());
            System.out.println("  Leave Taken : " + emp.getLeaveTaken() + " day(s)");
            System.out.printf("  Net Salary  : Rs.%,.2f%n", emp.calculateSalary());
            System.out.println("  " + PayrollConstants.HORIZONTAL_RULE + "\n");

        } catch (EmployeeNotFoundException ex) {
            System.out.println("\n  Error: " + ex.getMessage() + "\n");
        }
    }

    // ========================= 4. UPDATE EMPLOYEE =========================
    private static void updateEmployee() {
        System.out.println("\n" + PayrollConstants.HORIZONTAL_RULE);
        System.out.println("  UPDATE EMPLOYEE");
        System.out.println(PayrollConstants.HORIZONTAL_RULE);

        try {
            int id = readIntInput("  Enter Employee ID to update: ");
            Employee existing = employeeService.searchEmployee(id);

            System.out.println("  (Press Enter to keep current value)\n");

            String name = readOptionalString("  Name [" + existing.getName() + "]: ", existing.getName());
            ValidationUtil.validateName(name);

            String dept = readOptionalString("  Department [" + existing.getDepartment() + "]: ", existing.getDepartment());
            ValidationUtil.validateNonBlank(dept, "Department");

            String desig = readOptionalString("  Designation [" + existing.getDesignation() + "]: ", existing.getDesignation());
            ValidationUtil.validateNonBlank(desig, "Designation");

            double salary = readOptionalDouble("  Base Salary [" + existing.getBaseSalary() + "]: ", existing.getBaseSalary());
            ValidationUtil.validatePositiveSalary(salary);

            int leave = readOptionalInt("  Leave Taken [" + existing.getLeaveTaken() + "]: ", existing.getLeaveTaken());
            ValidationUtil.validateLeave(leave);

            // Create updated employee of the same type
            Employee updated = switch (existing.getEmployeeType()) {
                case REGULAR  -> new RegularEmployee(id, name, dept, desig, salary, leave);
                case CONTRACT -> new ContractEmployee(id, name, dept, desig, salary, leave);
                case INTERN   -> new Intern(id, name, dept, desig, salary, leave);
            };

            employeeService.updateEmployee(updated);
            System.out.println("\n  Employee ID " + id + " updated successfully.\n");

        } catch (EmployeeNotFoundException | InvalidSalaryException | InvalidLeaveException ex) {
            System.out.println("\n  Error: " + ex.getMessage() + "\n");
        } catch (IllegalArgumentException ex) {
            System.out.println("\n  Validation Error: " + ex.getMessage() + "\n");
        }
    }

    // ========================= 5. DELETE EMPLOYEE =========================
    private static void deleteEmployee() {
        System.out.println("\n" + PayrollConstants.HORIZONTAL_RULE);
        System.out.println("  DELETE EMPLOYEE");
        System.out.println(PayrollConstants.HORIZONTAL_RULE);

        try {
            int id = readIntInput("  Enter Employee ID to delete: ");
            Employee emp = employeeService.searchEmployee(id);

            System.out.print("  Are you sure you want to delete '" + emp.getName() + "' (ID: " + id + ")? (y/n): ");
            String confirm = scanner.nextLine().trim();

            if (confirm.equalsIgnoreCase("y")) {
                employeeService.deleteEmployee(id);
                System.out.println("\n  Employee ID " + id + " deleted successfully.\n");
            } else {
                System.out.println("\n  Deletion cancelled.\n");
            }

        } catch (EmployeeNotFoundException ex) {
            System.out.println("\n  Error: " + ex.getMessage() + "\n");
        }
    }

    // ========================= 6. GENERATE PAYSLIP =========================
    private static void generatePayslip() {
        System.out.println("\n" + PayrollConstants.HORIZONTAL_RULE);
        System.out.println("  GENERATE PAYSLIP");
        System.out.println(PayrollConstants.HORIZONTAL_RULE);

        try {
            int id = readIntInput("  Enter Employee ID: ");
            Employee emp = employeeService.searchEmployee(id);

            // Generate payslip (polymorphic - salary calculated based on actual type)
            String payslip = payrollService.generatePayslip(emp);
            System.out.println(payslip);

            // Ask if user wants to save to file
            System.out.print("  Export payslip to file? (y/n): ");
            String export = scanner.nextLine().trim();

            if (export.equalsIgnoreCase("y")) {
                fileService.exportPayslip(emp, payslip);
            }
            System.out.println();

        } catch (EmployeeNotFoundException ex) {
            System.out.println("\n  Error: " + ex.getMessage() + "\n");
        } catch (FileProcessingException ex) {
            System.out.println("\n  File Error: " + ex.getMessage() + "\n");
        }
    }

    // ========================= 7. BULK IMPORT =========================
    private static void bulkImportEmployees() {
        System.out.println("\n" + PayrollConstants.HORIZONTAL_RULE);
        System.out.println("  BULK IMPORT FROM FILE");
        System.out.println(PayrollConstants.HORIZONTAL_RULE);

        try {
            fileService.loadEmployeesFromFile(employeeService);
        } catch (FileProcessingException ex) {
            System.out.println("\n  Import Error: " + ex.getMessage() + "\n");
        }
        System.out.println();
    }

    // ========================= 8. EXPORT DATA =========================
    private static void exportEmployeeData() {
        System.out.println("\n" + PayrollConstants.HORIZONTAL_RULE);
        System.out.println("  EXPORT EMPLOYEE DATA");
        System.out.println(PayrollConstants.HORIZONTAL_RULE);

        if (employeeService.getEmployeeCount() == 0) {
            System.out.println("\n  No employees to export.\n");
            return;
        }

        try {
            fileService.saveEmployees(employeeService);
        } catch (FileProcessingException ex) {
            System.out.println("\n  Export Error: " + ex.getMessage() + "\n");
        }
        System.out.println();
    }

    // ========================= 9. PAYROLL SUMMARY =========================
    private static void displayPayrollSummary() {
        List<Employee> employees = employeeService.viewAllEmployees();
        String summary = payrollService.displayPayrollSummary(employees);
        System.out.println(summary);
    }

    // ========================= INPUT HELPER METHODS =========================
    // These helper methods handle bad input gracefully (no crashes)

    // Reads an integer, keeps asking if user types non-numeric input
    private static int readIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("  Please enter a valid integer.\n");
            }
        }
    }

    // Reads a double value
    private static double readDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("  Please enter a valid number.\n");
            }
        }
    }

    // Reads a non-empty string
    private static String readStringInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("  Input cannot be empty.\n");
        }
    }

    // Reads optional string - returns default if user presses Enter
    private static String readOptionalString(String prompt, String defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? defaultValue : input;
    }

    // Reads optional int - returns default if user presses Enter
    private static int readOptionalInt(String prompt, int defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return defaultValue;
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            System.out.println("  Invalid number - keeping current value.");
            return defaultValue;
        }
    }

    // Reads optional double - returns default if user presses Enter
    private static double readOptionalDouble(String prompt, double defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return defaultValue;
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException ex) {
            System.out.println("  Invalid number - keeping current value.");
            return defaultValue;
        }
    }
}
