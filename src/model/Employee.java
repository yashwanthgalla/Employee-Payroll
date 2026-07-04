package model;

import enums.EmployeeType;
import java.util.Objects;

/*
 * Employee.java  (Abstract Class)
 * --------------------------------
 * This is the PARENT class for all employee types.
 * It is "abstract" because we never create a plain "Employee" object.
 * Instead, we create RegularEmployee, ContractEmployee, or Intern.
 *
 * OOP Concepts Used:
 *   - Abstraction     → abstract class with abstract method calculateSalary()
 *   - Encapsulation   → private fields with public getters/setters
 *   - Inheritance     → subclasses extend this class
 *   - Polymorphism    → each subclass overrides calculateSalary() differently
 */
public abstract class Employee {

    // Private fields - can only be accessed through getters/setters (Encapsulation)
    private int id;
    private String name;
    private String department;
    private String designation;
    private double baseSalary;
    private int leaveTaken;

    // Default constructor
    protected Employee() { }

    // Parameterized constructor - sets all fields at once
    protected Employee(int id, String name, String department,
                       String designation, double baseSalary, int leaveTaken) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.department = Objects.requireNonNull(department, "Department cannot be null");
        this.designation = Objects.requireNonNull(designation, "Designation cannot be null");
        this.baseSalary = baseSalary;
        this.leaveTaken = leaveTaken;
    }

    // --------- Abstract Methods (must be implemented by child classes) ---------

    // Each subclass calculates salary differently - this is POLYMORPHISM
    public abstract double calculateSalary();

    // Each subclass returns its own type (REGULAR, CONTRACT, INTERN)
    public abstract EmployeeType getEmployeeType();

    // --------- Getters ---------

    public int getId()              { return id; }
    public String getName()         { return name; }
    public String getDepartment()   { return department; }
    public String getDesignation()  { return designation; }
    public double getBaseSalary()   { return baseSalary; }
    public int getLeaveTaken()      { return leaveTaken; }

    // --------- Setters ---------

    public void setId(int id)                      { this.id = id; }
    public void setName(String name)               { this.name = name; }
    public void setDepartment(String department)    { this.department = department; }
    public void setDesignation(String designation)  { this.designation = designation; }
    public void setBaseSalary(double baseSalary)    { this.baseSalary = baseSalary; }
    public void setLeaveTaken(int leaveTaken)       { this.leaveTaken = leaveTaken; }

    // --------- toString - prints employee details in a readable format ---------

    @Override
    public String toString() {
        return String.format("Employee{id=%d, name='%s', dept='%s', designation='%s', type=%s, salary=%.2f, leave=%d}",
                id, name, department, designation, getEmployeeType().getDisplayName(), baseSalary, leaveTaken);
    }

    // Two employees are considered equal if they have the same ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee other = (Employee) o;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
