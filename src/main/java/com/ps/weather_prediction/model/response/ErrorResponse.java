package com.ps.weather_prediction.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class ErrorResponse {
    CodeEnum code;
    String message;

    public enum CodeEnum {
        UNAUTHORIZED,
        FORBIDDEN,
        INPUT_ERROR,
        CITY_NOT_FOUND,
        TECHNICAL_FAILURE
    }
}