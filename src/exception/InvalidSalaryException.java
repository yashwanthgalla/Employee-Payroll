package exception;

// Thrown when salary value is invalid (zero or negative)
public class InvalidSalaryException extends Exception {
    public InvalidSalaryException(String message) {
        super(message);
    }
}
