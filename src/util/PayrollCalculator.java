package util;

import constants.PayrollConstants;
import model.Employee;

/*
 * PayrollCalculator.java
 * ----------------------
 * This class contains ONLY the salary calculation formulas.
 * It does NOT read user input (no Scanner) and does NOT do file operations.
 *
 * Why separate?
 * - Single Responsibility Principle: this class has ONE job → calculate salary
 * - Easy to test: just pass numbers in, check numbers out
 * - Easy to change: if tax rate changes, update only PayrollConstants
 *
 * Key method: calculateNetSalary(employee)
 *   → Uses Java 17 switch expression to pick the right formula
 *   → No if-else chain needed — clean and readable
 */
public final class PayrollCalculator {

    private PayrollCalculator() { }  // Utility class - no objects needed

    // Tax = 10% of gross salary (only for Regular employees)
    public static double calculateTax(double grossSalary) {
        return grossSalary * PayrollConstants.REGULAR_TAX_RATE;
    }

    // Allowance = 20% of base salary (only for Regular employees)
    public static double calculateAllowance(double baseSalary) {
        return baseSalary * PayrollConstants.REGULAR_ALLOWANCE_RATE;
    }

    // Bonus = 10% of base salary (only for Contract employees)
    public static double calculateBonus(double baseSalary) {
        return baseSalary * PayrollConstants.CONTRACT_BONUS_RATE;
    }

    // Leave deduction = (baseSalary / 30) x number of leave days
    public static double calculateLeaveDeduction(double baseSalary, int leaveTaken) {
        return baseSalary * PayrollConstants.LEAVE_DEDUCTION_PER_DAY * leaveTaken;
    }

    /*
     * Main salary calculation method.
     * Uses Java 17 switch expression to apply the correct formula
     * based on employee type - this is where POLYMORPHISM meets clean code.
     *
     * Regular:  Gross(base + 20%) - Tax(10%) - Leave Deduction
     * Contract: Gross(base + 10%) - Leave Deduction
     * Intern:   Just the stipend (no deductions)
     */
    public static double calculateNetSalary(Employee employee) {
        double base  = employee.getBaseSalary();
        int    leave = employee.getLeaveTaken();

        return switch (employee.getEmployeeType()) {
            case REGULAR -> {
                double allowance      = calculateAllowance(base);
                double gross          = base + allowance;
                double tax            = calculateTax(gross);
                double leaveDeduction = calculateLeaveDeduction(base, leave);
                yield gross - tax - leaveDeduction;   // yield = return value from switch block
            }
            case CONTRACT -> {
                double bonus          = calculateBonus(base);
                double gross          = base + bonus;
                double leaveDeduction = calculateLeaveDeduction(base, leave);
                yield gross - leaveDeduction;
            }
            case INTERN -> base;   // Stipend only - no deductions
        };
    }
}
