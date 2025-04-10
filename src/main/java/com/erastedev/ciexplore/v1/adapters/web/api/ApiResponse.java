package com.erastedev.ciexplore.v1.adapters.web.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {

    private int status;
    private String message;
    private T data;
    private String errors;
    private HashMap<String, String> errorDetails = new HashMap<>();
    private boolean success;


    public ApiResponse() {
    }
}
