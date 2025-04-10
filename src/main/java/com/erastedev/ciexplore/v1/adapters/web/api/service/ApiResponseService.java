package com.erastedev.ciexplore.v1.adapters.web.api.service;

import com.erastedev.ciexplore.v1.adapters.web.api.ApiResponse;
import com.erastedev.ciexplore.v1.adapters.web.api.builder.ApiBuilder;
import com.erastedev.ciexplore.v1.adapters.web.api.builder.ErrorDetailBuilder;
import com.erastedev.ciexplore.v1.domain.ports.out.logs.RequestLoggingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;

@Service
public class ApiResponseService {

    private static final Logger logger = LoggerFactory.getLogger(ApiResponseService.class);
    private final Environment env;

    public ApiResponseService(Environment env, RequestLoggingService requestLoggingService) {
        this.env = env;
    }

    /**
     * This service provides a wrapper for the ResponseEntity to have a more
     * meaningful response structure in the API.
     */
    public <T> ResponseEntity<ApiResponse<T>> success(String message, T data, HttpStatus status) {
        if (Boolean.parseBoolean(env.getProperty("custom.logging.enabled"))) {
            logger.info("Success Response from {}: status={}, message={}, data={}", ApiResponseService.class, status, message, data);
        }

        ApiResponse<T> response = new ApiBuilder<T>()
                .withStatus(status.value())
                .withMessage(message)
                .withData(data)
                .withSuccess(true)
                .build();

        return new ResponseEntity<>(response, status);
    }

    /**
     * This service provides a wrapper for the ResponseEntity to have a more
     * meaningful response structure in the API.
     */
    public <T> ResponseEntity<ApiResponse<T>> success(String message, HttpStatus status) {
        return success(message, null, status);
    }

    /**
     * This service provides a wrapper for the ResponseEntity to have a more
     * meaningful response structure in the API.
     */
    public <T> ResponseEntity<ApiResponse<T>> error(String message, String error, HashMap<String, String> errorDetails, HttpStatus status) {
        if (Boolean.parseBoolean(env.getProperty("custom.logging.enabled"))) {
            logger.error("Error Response from {}: status={}, message={}, error={}", ApiResponseService.class, status, message, error);
        }

        ApiResponse<T> response = new ApiBuilder<T>()
                .withStatus(status.value())
                .withMessage(message)
                .withError(error)
                .withErrorDetails(errorDetails)
                .withSuccess(false)
                .build();

        return new ResponseEntity<>(response, status);
    }

    /**
     * This service provides a wrapper for the ResponseEntity to have a more
     * meaningful response structure in the API.
     */
    public <T> ResponseEntity<ApiResponse<T>> internalError(String message, Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        logger.error("Error Response from {}: status={}, message={}, error={}", ApiResponseService.class, status, message, e.getMessage());

        ApiResponse<T> response = new ApiBuilder<T>()
                .withData(null)
                .withStatus(status.value())
                .withMessage(message == null ? e.getMessage() : message)
                .withError(e.getMessage())
                .withErrorDetails(
                        new ErrorDetailBuilder()
                                .add("cause", e.getCause() != null ? e.getCause().toString() : Arrays.stream(e.getStackTrace()).findFirst().get().toString())
                                .add("message", e.getMessage() != null ? e.getMessage() : null)
                                .add("Files", Arrays.toString(e.getStackTrace()))
                                .getErrorDetails()
                )
                .withSuccess(false)
                .build();

        return new ResponseEntity<>(response, status);
    }
}
