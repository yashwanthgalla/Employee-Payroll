package web;

import enums.EmployeeType;
import exception.DuplicateEmployeeException;
import exception.EmployeeNotFoundException;
import exception.FileProcessingException;
import exception.InvalidLeaveException;
import exception.InvalidSalaryException;
import model.ContractEmployee;
import model.Employee;
import model.Intern;
import model.RegularEmployee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.EmployeeService;
import service.FileService;
import util.ValidationUtil;
import web.dto.EmployeeRequest;
import web.dto.EmployeeResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final FileService fileService;

    public EmployeeController(EmployeeService employeeService, FileService fileService) {
        this.employeeService = employeeService;
        this.fileService = fileService;
    }

    @GetMapping
    public List<EmployeeResponse> getAllEmployees() {
        return employeeService.viewAllEmployees()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public EmployeeResponse getEmployeeById(@PathVariable int id) throws EmployeeNotFoundException {
        return toResponse(employeeService.searchEmployee(id));
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> addEmployee(@RequestBody EmployeeRequest request)
            throws DuplicateEmployeeException, InvalidSalaryException, InvalidLeaveException, FileProcessingException {
        Employee employee = toEmployee(request, request.id());
        employeeService.addEmployee(employee);
        fileService.saveEmployees(employeeService);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(employee));
    }

    @PutMapping("/{id}")
    public EmployeeResponse updateEmployee(@PathVariable int id, @RequestBody EmployeeRequest request)
            throws EmployeeNotFoundException, InvalidSalaryException, InvalidLeaveException, FileProcessingException {
        Employee updatedEmployee = toEmployee(request, id);
        employeeService.updateEmployee(updatedEmployee);
        fileService.saveEmployees(employeeService);
        return toResponse(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteEmployee(@PathVariable int id)
            throws EmployeeNotFoundException, FileProcessingException {
        employeeService.deleteEmployee(id);
        fileService.saveEmployees(employeeService);
        return Map.of("message", "Employee ID " + id + " deleted successfully.");
    }

    private Employee toEmployee(EmployeeRequest request, int id)
            throws InvalidSalaryException, InvalidLeaveException {
        ValidationUtil.validatePositiveId(id);
        ValidationUtil.validateName(request.name());
        ValidationUtil.validateNonBlank(request.department(), "Department");
        ValidationUtil.validateNonBlank(request.designation(), "Designation");
        ValidationUtil.validatePositiveSalary(request.baseSalary());
        ValidationUtil.validateLeave(request.leaveTaken());

        EmployeeType type = EmployeeType.fromString(request.employeeType());

        return switch (type) {
            case REGULAR -> new RegularEmployee(id, request.name(), request.department(), request.designation(), request.baseSalary(), request.leaveTaken());
            case CONTRACT -> new ContractEmployee(id, request.name(), request.department(), request.designation(), request.baseSalary(), request.leaveTaken());
            case INTERN -> new Intern(id, request.name(), request.department(), request.designation(), request.baseSalary(), request.leaveTaken());
        };
    }

    private EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getDepartment(),
                employee.getDesignation(),
                employee.getEmployeeType().getDisplayName(),
                employee.getBaseSalary(),
                employee.getLeaveTaken(),
                employee.calculateSalary()
        );
    }
}