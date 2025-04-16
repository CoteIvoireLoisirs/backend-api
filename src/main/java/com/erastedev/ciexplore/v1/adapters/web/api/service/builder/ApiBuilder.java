/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-28 14:45:53
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:45
 * @FilePath: src/main/java/ca/deltagis/success/v1/adapters/web/api/ApiResponseBuilder.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package com.erastedev.ciexplore.v1.adapters.web.api.service.builder;

import com.erastedev.ciexplore.v1.adapters.web.api.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;

public class ApiBuilder<T> {
    private int status;
    private String message;
    private T data;
    private String error;
    private HashMap<String, String> errorDetails;
    private boolean success;

    private Logger logger = LoggerFactory.getLogger(ApiBuilder.class);

    /**
     * Set the status of the ApiResponse.
     *
     * @param status The status to be set.
     * @return The ApiResponseBuilder instance.
     */
    public ApiBuilder<T> withStatus(int status) {
        this.status = status;
        return this;
    }

    /**
     * Set the message of the ApiResponse.
     *
     * @param message The message to be set.
     * @return The ApiResponseBuilder instance.
     */
    public ApiBuilder<T> withMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * Set the data of the ApiResponse.
     *
     * @param data The data to be set.
     * @return The ApiResponseBuilder instance.
     */
    public ApiBuilder<T> withData(T data) {
        this.data = data;
        return this;
    }

    /**
     * Set the error message of the ApiResponse.
     *
     * @param error The error message.
     * @return The ApiResponseBuilder instance.
     */
    public ApiBuilder<T> withError(String error) {
        this.error = error;
        return this;
    }

    /**
     * Set the error details of the ApiResponse.
     *
     * @param errorDetails A HashMap containing the error details.
     * @return The ApiResponseBuilder instance.
     */
    public ApiBuilder<T> withErrorDetails(HashMap<String, String> errorDetails) {
        this.errorDetails = errorDetails;
        return this;
    }

    /**
     * Set the success status of the ApiResponse.
     *
     * @param success Whether the ApiResponse is successful.
     * @return The ApiResponseBuilder instance.
     */
    public ApiBuilder<T> withSuccess(boolean success) {
        this.success = success;
        return this;
    }

    /**
     * Set the success status of the ApiResponse.
     *
     * @return The ApiResponseBuilder instance.
     */
    public ApiResponse<T> build() {
        return new ApiResponse<>(status, message, data, error, errorDetails, success);
    }

    /**
     * Builds the ApiResponse and wraps it into a ResponseEntity.
     *
     * @return ResponseEntity<ApiResponse < T>> with the specified HTTP status.
     */
    public ResponseEntity<ApiResponse<T>> buildWithResponseEntity() {
        return new ResponseEntity<>(build(), HttpStatus.valueOf(status));
    }

    /**
     * Builds the ApiResponse with HTTP status 200 (OK) and wraps it into a ResponseEntity.
     *
     * @param message The success message.
     * @param data    The data to be included in the response.
     * @return ResponseEntity<ApiResponse < T>> with HTTP status 200.
     */
    public ResponseEntity<ApiResponse<T>> success(String message, T data) {
        this.status = HttpStatus.OK.value();
        this.success = true;
        this.message = message;
        this.data = data;
        return new ResponseEntity<>(build(), HttpStatus.OK);
    }

    /**
     * Builds the ApiResponse with HTTP status 400 (Bad Request) and wraps it into a ResponseEntity.
     *
     * @return ResponseEntity<ApiResponse < T>> with HTTP status 400.
     */
    public ResponseEntity<ApiResponse<T>> badResponse(String error, String message) {
        logger.info("Error Response from ApiBuilder: status={}, message={}, error={}", HttpStatus.BAD_REQUEST, message, error);

        this.status = HttpStatus.BAD_REQUEST.value();
        this.success = false;
        this.error = error;
        this.message = message;

        // If errorDetails is null, create a new ErrorDetailBuilder and add the message
        if (this.errorDetails == null) {
            this.errorDetails = new ErrorDetailBuilder()
                    .add(error, message)
                    .build();
        }
        return new ResponseEntity<>(build(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ApiResponse<T>> internalServerError(String message, Exception e) {
        try {
            e.printStackTrace();

            this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
            this.success = false;
            this.error = error;
            this.message = message;

            // If errorDetails is null, create a new ErrorDetailBuilder and add the message
            if (this.errorDetails == null) {
                this.errorDetails = new ErrorDetailBuilder()
                        .add("cause", e.getCause() != null ? e.getCause().toString() : Arrays.stream(e.getStackTrace()).findFirst().get().toString())
                        .add("message", e.getMessage() != null ? e.getMessage() : "Something went wrong")
                        .build();
            }
            return new ResponseEntity<>(build(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            logger.error("Error Response from ApiBuilder: status={}, message={}, error={}", HttpStatus.INTERNAL_SERVER_ERROR, message, ex.getMessage());
            return new ResponseEntity<>(build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Builds the ApiResponse with HTTP status 404 (Not Found) and wraps it into a ResponseEntity.
     *
     * @param error   The error message to be set in the ApiResponse.
     * @param message The detailed message to provide additional context for the error.
     * @return ResponseEntity<ApiResponse < T>> with HTTP status 404.
     */
    public ResponseEntity<ApiResponse<T>> notFound(String error, String message) {
        this.status = HttpStatus.NOT_FOUND.value();
        this.success = false;
        this.error = error;
        this.message = message;

        // If errorDetails is null, create a new ErrorDetailBuilder and add the message
        if (this.errorDetails == null) {
            this.errorDetails = new ErrorDetailBuilder()
                    .add(error, message)
                    .build();
        }
        return new ResponseEntity<>(build(), HttpStatus.NOT_FOUND);
    }
}
