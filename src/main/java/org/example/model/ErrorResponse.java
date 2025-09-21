package org.example.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ErrorResponse {

    private String error;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getCode() {
        return error;
    }

    public void setCode(String error) {
        this.error = error;
    }

    public String toJson() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }
}
