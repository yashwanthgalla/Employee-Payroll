package web;

import exception.FileProcessingException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.builder.SpringApplicationBuilder;
import service.EmployeeService;
import service.FileService;
import service.PayrollService;

import java.util.Map;

@SpringBootApplication(scanBasePackages = {"web"})
public class PayrollApiApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(PayrollApiApplication.class)
                .properties(Map.of("server.port", "8000"))
                .run(args);
    }

    @Bean
    public EmployeeService employeeService() {
        return new EmployeeService();
    }

    @Bean
    public PayrollService payrollService() {
        return new PayrollService();
    }

    @Bean
    public FileService fileService() {
        return new FileService();
    }

    @Bean
    public CommandLineRunner preloadEmployees(EmployeeService employeeService, FileService fileService) {
        return args -> {
            try {
                fileService.loadEmployeesFromFile(employeeService);
            } catch (FileProcessingException ex) {
                System.out.println("  Startup import skipped: " + ex.getMessage());
            }
        };
    }
}