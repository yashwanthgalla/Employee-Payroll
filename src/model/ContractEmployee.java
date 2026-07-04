package model;

import enums.EmployeeType;
import util.PayrollCalculator;

/*
 * ContractEmployee.java
 * ---------------------
 * Represents a fixed-term contract employee.
 *
 * Salary Formula:
 *   Gross  = Base Salary + 10% Bonus
 *   Tax    = 0 (no tax for contract employees)
 *   Leave  = (Base / 30) x Days Taken
 *   Net    = Gross - Leave Deduction
 *
 * This class INHERITS from Employee and OVERRIDES calculateSalary() → Polymorphism
 */
public class ContractEmployee extends Employee {

    public ContractEmployee(int id, String name, String department,
                            String designation, double baseSalary, int leaveTaken) {
        super(id, name, department, designation, baseSalary, leaveTaken);
    }

    @Override
    public double calculateSalary() {
        return PayrollCalculator.calculateNetSalary(this);
    }

    @Override
    public EmployeeType getEmployeeType() {
        return EmployeeType.CONTRACT;
    }
}
