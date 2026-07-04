package exception;

// Thrown when someone tries to add an employee with an ID that already exists
public class DuplicateEmployeeException extends Exception {
    public DuplicateEmployeeException(String message) {
        super(message);
    }
}
