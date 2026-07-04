package service;

import constants.PayrollConstants;
import enums.EmployeeType;
import exception.FileProcessingException;
import model.ContractEmployee;
import model.Employee;
import model.Intern;
import model.RegularEmployee;
import util.ValidationUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/*
 * FileService.java
 * ----------------
 * Handles all file reading and writing operations using java.nio.file package.
 *
 * Three main operations:
 *   1. loadEmployeesFromFile()  → Reads employees from CSV file
 *   2. saveEmployees()          → Writes all employees back to CSV file
 *   3. exportPayslip()          → Saves a payslip as a text file
 *
 * Key features:
 *   - Uses BufferedReader/BufferedWriter (efficient I/O)
 *   - Uses try-with-resources (automatic resource cleanup)
 *   - Skips invalid CSV rows instead of crashing (resilient import)
 *   - Prints import summary (how many imported, how many skipped, why)
 */
public class FileService {

    /*
     * Reads employee data from data/employees.txt (CSV format)
     * Format: ID,Name,Department,Designation,Type,Salary,Leave
     *
     * If a row has bad data, it gets SKIPPED (not crash the whole import).
     * At the end, prints a summary of imported vs skipped records.
     */
    public void loadEmployeesFromFile(EmployeeService employeeService) throws FileProcessingException {
        Objects.requireNonNull(employeeService, "EmployeeService cannot be null.");

        Path filePath = Paths.get(PayrollConstants.EMPLOYEE_DATA_FILE);

        if (!Files.exists(filePath)) {
            throw new FileProcessingException("File not found: " + filePath.toAbsolutePath());
        }

        int imported = 0;
        int skipped  = 0;
        List<String> skippedReasons = new ArrayList<>();

        // try-with-resources: BufferedReader is automatically closed after use
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.isBlank()) continue;   // Skip empty lines

                try {
                    Employee employee = parseCsvLine(line, lineNumber);
                    employeeService.addEmployee(employee);
                    imported++;
                } catch (Exception ex) {
                    // Don't crash - just record why this row was skipped
                    skipped++;
                    skippedReasons.add("  Line " + lineNumber + ": " + line.trim() + " -> " + ex.getMessage());
                }
            }
        } catch (IOException ex) {
            throw new FileProcessingException("Failed to read file: " + filePath.toAbsolutePath(), ex);
        }

        // Print import summary
        System.out.println("\n" + PayrollConstants.HORIZONTAL_RULE);
        System.out.println("  BULK IMPORT SUMMARY");
        System.out.println(PayrollConstants.HORIZONTAL_RULE);
        System.out.println("  Successfully Imported : " + imported);
        System.out.println("  Skipped Records       : " + skipped);

        if (!skippedReasons.isEmpty()) {
            System.out.println("\n  Skipped Details:");
            for (String reason : skippedReasons) {
                System.out.println(reason);
            }
        }
        System.out.println(PayrollConstants.HORIZONTAL_RULE);
    }

    /*
     * Saves all employees from memory to the CSV file.
     * Overwrites the existing file with current data.
     */
    public void saveEmployees(EmployeeService employeeService) throws FileProcessingException {
        Objects.requireNonNull(employeeService, "EmployeeService cannot be null.");

        Path filePath = Paths.get(PayrollConstants.EMPLOYEE_DATA_FILE);

        try {
            Files.createDirectories(filePath.getParent());   // Create 'data/' folder if it doesn't exist

            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                for (Map.Entry<Integer, Employee> entry : employeeService.getEmployeeMap().entrySet()) {
                    Employee emp = entry.getValue();

                    // Write one CSV line: ID,Name,Department,Designation,Type,Salary,Leave
                    String csvLine = String.join(",",
                            String.valueOf(emp.getId()),
                            emp.getName(),
                            emp.getDepartment(),
                            emp.getDesignation(),
                            emp.getEmployeeType().name(),
                            String.valueOf(emp.getBaseSalary()),
                            String.valueOf(emp.getLeaveTaken()));

                    writer.write(csvLine);
                    writer.newLine();
                }
            }

            System.out.println("\n  Employee data exported successfully to: " + filePath.toAbsolutePath());

        } catch (IOException ex) {
            throw new FileProcessingException("Failed to save file: " + filePath.toAbsolutePath(), ex);
        }
    }

    /*
     * Exports a single payslip to a text file.
     * File name format: payslip_101_2026-07-03.txt
     * Saved inside: data/payslips/
     */
    public void exportPayslip(Employee employee, String payslipContent) throws FileProcessingException {
        Objects.requireNonNull(employee, "Employee cannot be null.");
        Objects.requireNonNull(payslipContent, "Payslip content cannot be null.");

        String fileName = "payslip_" + employee.getId() + "_" + LocalDate.now() + ".txt";
        Path payslipDir  = Paths.get(PayrollConstants.PAYSLIP_DIRECTORY);
        Path payslipFile = payslipDir.resolve(fileName);

        try {
            Files.createDirectories(payslipDir);   // Create payslips folder if needed

            try (BufferedWriter writer = Files.newBufferedWriter(payslipFile)) {
                writer.write(payslipContent);
            }

            System.out.println("  Payslip exported to: " + payslipFile.toAbsolutePath());

        } catch (IOException ex) {
            throw new FileProcessingException("Failed to export payslip for Employee ID " + employee.getId(), ex);
        }
    }

    // -------- Private Helper Method --------

    /*
     * Parses one CSV line into an Employee object.
     * Expected format: ID,Name,Department,Designation,Type,Salary,Leave
     * Example: 101,John Doe,IT,Developer,REGULAR,50000,2
     */
    private Employee parseCsvLine(String line, int lineNumber) throws Exception {
        String[] tokens = line.split(PayrollConstants.CSV_DELIMITER);

        if (tokens.length != PayrollConstants.CSV_COLUMN_COUNT) {
            throw new IllegalArgumentException("Expected " + PayrollConstants.CSV_COLUMN_COUNT + " columns, found " + tokens.length);
        }

        // Parse each column
        int id             = Integer.parseInt(tokens[0].trim());
        String name        = tokens[1].trim();
        String department  = tokens[2].trim();
        String designation = tokens[3].trim();
        EmployeeType type  = EmployeeType.fromString(tokens[4].trim());
        double salary      = Double.parseDouble(tokens[5].trim());
        int leave          = Integer.parseInt(tokens[6].trim());

        // Validate all parsed values
        ValidationUtil.validatePositiveId(id);
        ValidationUtil.validateName(name);
        ValidationUtil.validateNonBlank(department, "Department");
        ValidationUtil.validateNonBlank(designation, "Designation");
        ValidationUtil.validatePositiveSalary(salary);
        ValidationUtil.validateLeave(leave);

        // Create the right type of employee object using switch expression
        return switch (type) {
            case REGULAR  -> new RegularEmployee(id, name, department, designation, salary, leave);
            case CONTRACT -> new ContractEmployee(id, name, department, designation, salary, leave);
            case INTERN   -> new Intern(id, name, department, designation, salary, leave);
        };
    }
}
