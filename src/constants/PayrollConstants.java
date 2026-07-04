package constants;

/*
 * PayrollConstants.java
 * --------------------
 * This class stores all the fixed values (constants) used across the project.
 * Instead of writing numbers like 0.20 or 0.10 directly in the code (magic numbers),
 * we keep them here so they are easy to find and change in one place.
 */
public final class PayrollConstants {

    // Private constructor - prevents anyone from creating an object of this class
    private PayrollConstants() { }

    // -------- Salary Calculation Rates --------

    public static final double REGULAR_ALLOWANCE_RATE = 0.20;   // 20% allowance for Regular employees
    public static final double REGULAR_TAX_RATE       = 0.10;   // 10% tax for Regular employees
    public static final double CONTRACT_BONUS_RATE    = 0.10;   // 10% bonus for Contract employees
    public static final double LEAVE_DEDUCTION_PER_DAY = 1.0 / 30.0;  // 1 day out of 30

    // -------- File Paths --------

    public static final String EMPLOYEE_DATA_FILE = "data/employees.txt";
    public static final String PAYSLIP_DIRECTORY  = "data/payslips";

    // -------- CSV Format --------

    public static final String CSV_DELIMITER   = ",";
    public static final int    CSV_COLUMN_COUNT = 7;   // ID, Name, Dept, Designation, Type, Salary, Leave

    // -------- Display Formatting --------

    public static final String HORIZONTAL_RULE =
            "------------------------------------------------------------";

    public static final String DOUBLE_RULE =
            "============================================================";

    // Format strings for printing employee tables
    public static final String TABLE_HEADER_FORMAT = "%-8s %-20s %-12s %-15s %-10s %-10s%n";
    public static final String TABLE_ROW_FORMAT    = "%-8d %-20s %-12s %-15s %-10s %-10.2f%n";

    // -------- Validation Limits --------

    public static final int    MAX_LEAVE_DAYS  = 30;
    public static final int    MIN_EMPLOYEE_ID = 1;
    public static final String NAME_PATTERN    = "^[a-zA-Z ]+$";  // Only letters and spaces allowed
}
