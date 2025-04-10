package ca.deltagis.success;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for the CI Explore project.
 * This class initializes and runs the Spring Boot application.
 */
@EnableScheduling
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "com.erastedev.ci_explore API", version = "1.0", description = ""))
public class SuccessApplication {
    /**
     * The main method serves as the entry point for the Spring Boot application.
     * It initializes and runs the application using the provided arguments.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(SuccessApplication.class, args);
    }
}
