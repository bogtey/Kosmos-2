package com.example.surveysystem.services; // Убедитесь, что пакет соответствует вашему проекту

public class PhotoServiceException extends RuntimeException {
    public PhotoServiceException(String message) {
        super(message);
    }

    public PhotoServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}