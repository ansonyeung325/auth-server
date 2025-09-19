package org.example.model;

public class ErrorResponse {

    private String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getCode() {
        return error;
    }

    public void setCode(String error) {
        this.error = error;
    }
}
