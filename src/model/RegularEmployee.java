package model;

import enums.EmployeeType;
import util.PayrollCalculator;

/*
 * RegularEmployee.java
 * --------------------
 * Represents a full-time salaried employee.
 *
 * Salary Formula:
 *   Gross  = Base Salary + 20% Allowance
 *   Tax    = 10% of Gross
 *   Leave  = (Base / 30) x Days Taken
 *   Net    = Gross - Tax - Leave Deduction
 *
 * This class INHERITS from Employee and OVERRIDES calculateSalary() → Polymorphism
 */
public class RegularEmployee extends Employee {

    public RegularEmployee(int id, String name, String department,
                           String designation, double baseSalary, int leaveTaken) {
        super(id, name, department, designation, baseSalary, leaveTaken);  // calls parent constructor
    }

    // Overriding the abstract method - Java decides at runtime which version to call
    @Override
    public double calculateSalary() {
        return PayrollCalculator.calculateNetSalary(this);
    }

    @Override
    public EmployeeType getEmployeeType() {
        return EmployeeType.REGULAR;
    }
}
