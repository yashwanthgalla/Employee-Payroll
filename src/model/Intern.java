package model;

import enums.EmployeeType;
import util.PayrollCalculator;

/*
 * Intern.java
 * -----------
 * Represents a trainee / intern.
 *
 * Salary Formula:
 *   Net = Base Salary (monthly stipend, as-is)
 *   Tax = 0
 *   Leave Deduction = 0
 *
 * Simplest salary calculation - just returns the stipend amount.
 */
public class Intern extends Employee {

    public Intern(int id, String name, String department,
                  String designation, double baseSalary, int leaveTaken) {
        super(id, name, department, designation, baseSalary, leaveTaken);
    }

    @Override
    public double calculateSalary() {
        return PayrollCalculator.calculateNetSalary(this);
    }

    @Override
    public EmployeeType getEmployeeType() {
        return EmployeeType.INTERN;
    }
}
