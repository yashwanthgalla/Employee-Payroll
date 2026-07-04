package service;

import exception.DuplicateEmployeeException;
import exception.EmployeeNotFoundException;
import model.Employee;

import java.util.*;

/*
 * EmployeeService.java
 * --------------------
 * This is the MAIN service that manages all employee data.
 * It uses a HashMap<Integer, Employee> to store employees in memory.
 *
 * Why HashMap?
 * - Fast lookups by ID → O(1) time complexity
 * - Key = Employee ID, Value = Employee object
 * - Automatically prevents null keys
 *
 * Operations: Add, Update, Delete, Search, View All, Count, Clear
 *
 * Important: This service throws custom exceptions for error cases
 * instead of returning null or -1 (clean error handling).
 */
public class EmployeeService {

    // HashMap to store employees - Key: employee ID, Value: employee object
    private final Map<Integer, Employee> employeeMap;

    public EmployeeService() {
        this.employeeMap = new HashMap<>();
    }

    // -------- ADD --------
    // Throws DuplicateEmployeeException if ID already exists
    public void addEmployee(Employee employee) throws DuplicateEmployeeException {
        Objects.requireNonNull(employee, "Employee cannot be null.");
        int id = employee.getId();

        if (employeeMap.containsKey(id)) {
            throw new DuplicateEmployeeException("Employee with ID " + id + " already exists.");
        }
        employeeMap.put(id, employee);
    }

    // -------- UPDATE --------
    // Throws EmployeeNotFoundException if ID doesn't exist
    public void updateEmployee(Employee employee) throws EmployeeNotFoundException {
        Objects.requireNonNull(employee, "Employee cannot be null.");
        int id = employee.getId();

        if (!employeeMap.containsKey(id)) {
            throw new EmployeeNotFoundException("Cannot update - Employee ID " + id + " not found.");
        }
        employeeMap.put(id, employee);   // replaces the old record
    }

    // -------- DELETE --------
    public void deleteEmployee(int id) throws EmployeeNotFoundException {
        if (!employeeMap.containsKey(id)) {
            throw new EmployeeNotFoundException("Cannot delete - Employee ID " + id + " not found.");
        }
        employeeMap.remove(id);
    }

    // -------- SEARCH --------
    public Employee searchEmployee(int id) throws EmployeeNotFoundException {
        Employee employee = employeeMap.get(id);
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee with ID " + id + " not found.");
        }
        return employee;
    }

    // -------- VIEW ALL --------
    // Returns a sorted list (by ID) of all employees
    public List<Employee> viewAllEmployees() {
        List<Employee> employees = new ArrayList<>(employeeMap.values());
        employees.sort(Comparator.comparingInt(Employee::getId));   // Sort by ID ascending
        return Collections.unmodifiableList(employees);             // Prevents outside modification
    }

    // -------- COUNT --------
    public int getEmployeeCount() {
        return employeeMap.size();
    }

    // -------- CLEAR ALL --------
    public void clearEmployees() {
        employeeMap.clear();
    }

    // -------- CHECK EXISTS --------
    public boolean exists(int id) {
        return employeeMap.containsKey(id);
    }

    // Returns read-only view of the map (used by FileService for export)
    public Map<Integer, Employee> getEmployeeMap() {
        return Collections.unmodifiableMap(employeeMap);
    }
}
