package web.dto;

public record EmployeeRequest(
        int id,
        String name,
        String department,
        String designation,
        String employeeType,
        double baseSalary,
        int leaveTaken
) {
}