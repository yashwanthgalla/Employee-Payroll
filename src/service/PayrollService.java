package service;

import constants.PayrollConstants;
import enums.EmployeeType;
import model.Employee;
import util.PayrollCalculator;

import java.time.LocalDate;
import java.util.List;

/*
 * PayrollService.java
 * -------------------
 * This service handles payslip generation and payroll summary reports.
 *
 * Important design rule:
 * - This class NEVER stores employees. It receives them as parameters.
 * - It only does two things: generate payslips and create summary reports.
 * - This follows the Single Responsibility Principle (SRP).
 */
public class PayrollService {

    /*
     * Generates a formatted payslip for one employee.
     * Shows: personal details, earnings, deductions, and final net salary.
     * Returns the payslip as a String (can be printed or saved to file).
     */
    public String generatePayslip(Employee employee) {
        double baseSalary     = employee.getBaseSalary();
        EmployeeType type     = employee.getEmployeeType();
        double allowance      = 0.0;
        double bonus          = 0.0;
        double tax            = 0.0;
        double leaveDeduction = 0.0;
        double netSalary      = employee.calculateSalary();  // Polymorphic call!

        // Calculate individual components based on type
        switch (type) {
            case REGULAR -> {
                allowance      = PayrollCalculator.calculateAllowance(baseSalary);
                tax            = PayrollCalculator.calculateTax(baseSalary + allowance);
                leaveDeduction = PayrollCalculator.calculateLeaveDeduction(baseSalary, employee.getLeaveTaken());
            }
            case CONTRACT -> {
                bonus          = PayrollCalculator.calculateBonus(baseSalary);
                leaveDeduction = PayrollCalculator.calculateLeaveDeduction(baseSalary, employee.getLeaveTaken());
            }
            case INTERN -> { }  // No additional calculations for interns
        }

        // Build the payslip using StringBuilder (efficient string concatenation)
        StringBuilder sb = new StringBuilder();
        String line = PayrollConstants.DOUBLE_RULE;

        sb.append("\n").append(line).append("\n");
        sb.append("               EMPLOYEE PAYSLIP                \n");
        sb.append(line).append("\n");
        sb.append(String.format("  Date             : %s%n", LocalDate.now()));
        sb.append(String.format("  Employee ID      : %d%n", employee.getId()));
        sb.append(String.format("  Name             : %s%n", employee.getName()));
        sb.append(String.format("  Department       : %s%n", employee.getDepartment()));
        sb.append(String.format("  Designation      : %s%n", employee.getDesignation()));
        sb.append(String.format("  Employee Type    : %s%n", type.getDisplayName()));
        sb.append(PayrollConstants.HORIZONTAL_RULE).append("\n");
        sb.append("  EARNINGS\n");
        sb.append(PayrollConstants.HORIZONTAL_RULE).append("\n");
        sb.append(String.format("  Base Salary      : Rs.%,12.2f%n", baseSalary));

        if (type == EmployeeType.REGULAR) {
            sb.append(String.format("  Allowance (20%%)  : Rs.%,12.2f%n", allowance));
        }
        if (type == EmployeeType.CONTRACT) {
            sb.append(String.format("  Bonus (10%%)      : Rs.%,12.2f%n", bonus));
        }

        sb.append(PayrollConstants.HORIZONTAL_RULE).append("\n");
        sb.append("  DEDUCTIONS\n");
        sb.append(PayrollConstants.HORIZONTAL_RULE).append("\n");

        if (type == EmployeeType.REGULAR) {
            sb.append(String.format("  Tax (10%%)        : Rs.%,12.2f%n", tax));
        } else {
            sb.append(String.format("  Tax              : Rs.%,12.2f  (Exempt)%n", 0.0));
        }

        if (type != EmployeeType.INTERN) {
            sb.append(String.format("  Leave Deduction  : Rs.%,12.2f  (%d day(s))%n",
                    leaveDeduction, employee.getLeaveTaken()));
        } else {
            sb.append(String.format("  Leave Deduction  : Rs.%,12.2f  (N/A)%n", 0.0));
        }

        sb.append(PayrollConstants.DOUBLE_RULE).append("\n");
        sb.append(String.format("  NET SALARY       : Rs.%,12.2f%n", netSalary));
        sb.append(PayrollConstants.DOUBLE_RULE).append("\n");

        return sb.toString();
    }

    /*
     * Creates a summary table showing all employees with their base and net salaries.
     * Also shows totals at the bottom.
     */
    public String displayPayrollSummary(List<Employee> employees) {
        if (employees.isEmpty()) {
            return "\n  No employees found to generate payroll summary.\n";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(PayrollConstants.DOUBLE_RULE).append("\n");
        sb.append("               PAYROLL SUMMARY REPORT               \n");
        sb.append(String.format("               Date: %s%n", LocalDate.now()));
        sb.append(PayrollConstants.DOUBLE_RULE).append("\n");
        sb.append(String.format("%-8s %-20s %-12s %-12s %-14s%n",
                "ID", "Name", "Type", "Base Salary", "Net Salary"));
        sb.append(PayrollConstants.HORIZONTAL_RULE).append("\n");

        double totalBase = 0;
        double totalNet  = 0;

        for (Employee emp : employees) {
            double netSalary = emp.calculateSalary();
            sb.append(String.format("%-8d %-20s %-12s Rs.%,10.2f  Rs.%,12.2f%n",
                    emp.getId(), emp.getName(),
                    emp.getEmployeeType().getDisplayName(),
                    emp.getBaseSalary(), netSalary));
            totalBase += emp.getBaseSalary();
            totalNet  += netSalary;
        }

        sb.append(PayrollConstants.HORIZONTAL_RULE).append("\n");
        sb.append(String.format("  Total Employees  : %d%n", employees.size()));
        sb.append(String.format("  Total Base Salary: Rs.%,12.2f%n", totalBase));
        sb.append(String.format("  Total Net Payout : Rs.%,12.2f%n", totalNet));
        sb.append(PayrollConstants.DOUBLE_RULE).append("\n");

        return sb.toString();
    }
}
