package exception;

// Thrown when file reading or writing fails (wraps IOException)
public class FileProcessingException extends Exception {

    public FileProcessingException(String message) {
        super(message);
    }

    // This constructor lets us chain the original IOException as the "cause"
    public FileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
