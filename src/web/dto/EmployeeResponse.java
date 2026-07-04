package web.dto;

public record EmployeeResponse(
        int id,
        String name,
        String department,
        String designation,
        String employeeType,
        double baseSalary,
        int leaveTaken,
        double netSalary
) {
}