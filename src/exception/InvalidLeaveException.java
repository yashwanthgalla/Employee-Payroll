package exception;

// Thrown when leave days are invalid (negative or more than 30)
public class InvalidLeaveException extends Exception {
    public InvalidLeaveException(String message) {
        super(message);
    }
}
