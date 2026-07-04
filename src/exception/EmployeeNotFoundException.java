package exception;

// Thrown when we search for an employee ID that doesn't exist in the system
public class EmployeeNotFoundException extends Exception {
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
