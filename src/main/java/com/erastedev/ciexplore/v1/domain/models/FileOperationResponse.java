package com.erastedev.ciexplore.v1.domain.models;


import lombok.Data;

@Data
public class FileOperationResponse {

    private boolean success;
    private String message;

    // Constructeur avec tous les champs
    public FileOperationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getter pour le statut de réussite
    public boolean isSuccess() {
        return success;
    }

    // Setter pour le statut de réussite
    public void setSuccess(boolean success) {
        this.success = success;
    }

    // Getter pour le message
    public String getMessage() {
        return message;
    }

    // Setter pour le message
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "FileOperationResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
    


