package com.snowflake_guide.tourmate.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ValidationErrorResponse {
    private List<FieldError> errors;

    @Data
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String message;
    }
}

