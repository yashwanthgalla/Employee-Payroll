package enums;

/*
 * EmployeeType.java
 * -----------------
 * Enum representing the 3 types of employees in our system.
 * Each type has a display name (e.g., "Regular") for printing in reports.
 *
 * Why use an enum?
 * - It restricts employee types to only these 3 valid values.
 * - No one can accidentally pass "FULLTIME" or a random string.
 */
public enum EmployeeType {

    REGULAR("Regular"),
    CONTRACT("Contract"),
    INTERN("Intern");

    private final String displayName;

    EmployeeType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /*
     * Converts a string like "REGULAR" or "regular" into the matching enum value.
     * Uses Java 17 switch expression (arrow syntax).
     */
    public static EmployeeType fromString(String type) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Employee type cannot be null or blank.");
        }
        return switch (type.trim().toUpperCase()) {
            case "REGULAR"  -> REGULAR;
            case "CONTRACT" -> CONTRACT;
            case "INTERN"   -> INTERN;
            default -> throw new IllegalArgumentException(
                    "Unknown employee type: '" + type + "'. Expected REGULAR, CONTRACT, or INTERN.");
        };
    }
}
