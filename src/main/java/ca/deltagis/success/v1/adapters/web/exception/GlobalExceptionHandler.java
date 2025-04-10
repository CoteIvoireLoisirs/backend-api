package ca.deltagis.success.v1.adapters.web.exception;

import ca.deltagis.success.v1.adapters.web.api.ApiResponse;
import ca.deltagis.success.v1.adapters.web.api.service.ApiResponseService;
import ca.deltagis.success.v1.adapters.web.api.builder.ErrorDetailBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private ApiResponseService response;

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle all exceptions thrown by the application in a single place.
     * The response is a JSON payload that contains the error message and the HTTP status code.
     * The method is annotated with @ExceptionHandler to indicate that it should catch all exceptions.
     * The method is also annotated with @ControllerAdvice to indicate that it should be used by the controller.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String error = "Invalid parameter type.  Expected integer, received: " + ex.getValue();
        ErrorDetailBuilder errorDetails = new ErrorDetailBuilder();
        errorDetails.add("parameter", ex.getName());
        errorDetails.add("expectedType", Objects.requireNonNull(ex.getRequiredType()).getSimpleName());
        errorDetails.add("value", Objects.requireNonNull(ex.getValue()).toString());
        errorDetails.add("message", ex.getMessage());

        return response.error(error, "INVALID_INPUT", errorDetails.build(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle generic exceptions that are not explicitly handled by other methods.
     * Logs the error and returns a standardized error response with HTTP status 500.
     *
     * @param e the exception that was thrown
     * @return ResponseEntity containing error details and HTTP status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception e) {
        logger.error("An unexpected error occurred", e);
        ErrorDetailBuilder errorDetails = new ErrorDetailBuilder()
                .add("message", e.getMessage())
                .add("cause", e.getCause() != null ? e.getCause().toString() : null);
                //.add("stackTrace", Arrays.toString(e.getStackTrace()));

        return response.error(
                "Internal Server Error",
                "INTERNAL_SERVER_ERROR",
                errorDetails.build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
