package com.neondomain.technicalexam.model;

import java.util.Arrays;
import java.util.List;

public class ErrorResponse {
    private String message;
    private List<String> details;

    public ErrorResponse(String message, List<String> details) {
        this.message = message;
        this.details = details;
    }

    public ErrorResponse(String message, String detail) {
        this.message = message;
        this.details = Arrays.asList(detail);
    }

    public String getMessage() {
        return message;
    }

    public List<String> getDetails() {
        return details;
    }
}