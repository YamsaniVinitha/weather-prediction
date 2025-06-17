package com.ps.weather_prediction.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.ps.weather_prediction.model.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.ps.weather_prediction.model.response.ErrorResponse.CodeEnum.CITY_NOT_FOUND;
import static com.ps.weather_prediction.model.response.ErrorResponse.CodeEnum.INPUT_ERROR;
import static com.ps.weather_prediction.model.response.ErrorResponse.CodeEnum.TECHNICAL_FAILURE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<Object> handleJsonParseException(JsonParseException ex) {
        return getErrorResponse(ex.getMessage(), INTERNAL_SERVER_ERROR, TECHNICAL_FAILURE);
    }

    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<Object> handleCityNotFoundException(CityNotFoundException ex) {
        return getErrorResponse(ex.getMessage(), NOT_FOUND, CITY_NOT_FOUND);
    }

    @ExceptionHandler(CityWeatherDataUnavailableException.class)
    public ResponseEntity<Object> handleCityWeatherDataUnavailableException(CityWeatherDataUnavailableException ex) {
        log.info(ex.getMessage());
        return getErrorResponse(ex.getMessage(), INTERNAL_SERVER_ERROR, TECHNICAL_FAILURE);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentNotValidException.class, IllegalArgumentException.class})
    public ResponseEntity<Object> handleBadRequest(Exception ex) {
        return getErrorResponse(
                ex.getMessage() != null ? ex.getMessage() : "Bad request",
                BAD_REQUEST,
                INPUT_ERROR
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        return getErrorResponse("An unexpected error occurred: " + ex.getMessage(), INTERNAL_SERVER_ERROR, TECHNICAL_FAILURE);
    }

    private ResponseEntity<Object> getErrorResponse(String message, HttpStatusCode status, ErrorResponse.CodeEnum errorCode) {
        var errorResponse = ErrorResponse.builder()
                .message(message)
                .code(errorCode)
                .build();

        var headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        return new ResponseEntity<>(errorResponse, headers, status);
    }
}

