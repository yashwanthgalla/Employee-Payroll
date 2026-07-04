package util;

import exception.InvalidLeaveException;
import exception.InvalidSalaryException;
import constants.PayrollConstants;
import java.util.Objects;

/*
 * ValidationUtil.java
 * -------------------
 * This utility class checks if user input is valid BEFORE we use it.
 * If the input is bad, it throws a custom exception with a clear message.
 *
 * Why validate?
 * - Prevents crashes from bad data (negative salary, empty name, etc.)
 * - Gives the user helpful error messages instead of ugly stack traces
 *
 * All methods are static - we don't need to create an object to use them.
 */
public final class ValidationUtil {

    private ValidationUtil() { }  // Can't create objects of utility class

    // Checks that salary is a positive number
    public static void validatePositiveSalary(double salary) throws InvalidSalaryException {
        if (salary <= 0) {
            throw new InvalidSalaryException("Salary must be positive. You entered: " + salary);
        }
    }

    // Checks that employee ID is at least 1
    public static void validatePositiveId(int id) {
        if (id < PayrollConstants.MIN_EMPLOYEE_ID) {
            throw new IllegalArgumentException("Employee ID must be at least 1. You entered: " + id);
        }
    }

    // Checks that name contains only letters and spaces (no numbers or symbols)
    public static void validateName(String name) {
        Objects.requireNonNull(name, "Name cannot be null.");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank.");
        }
        if (!name.matches(PayrollConstants.NAME_PATTERN)) {
            throw new IllegalArgumentException("Name must contain only letters and spaces. You entered: '" + name + "'");
        }
    }

    // Checks that leave days are between 0 and 30
    public static void validateLeave(int leave) throws InvalidLeaveException {
        if (leave < 0) {
            throw new InvalidLeaveException("Leave days cannot be negative. You entered: " + leave);
        }
        if (leave > PayrollConstants.MAX_LEAVE_DAYS) {
            throw new InvalidLeaveException("Leave days cannot exceed " + PayrollConstants.MAX_LEAVE_DAYS + ". You entered: " + leave);
        }
    }

    // Checks that a text field is not null or blank
    public static void validateNonBlank(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null.");
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
        }
    }
}
