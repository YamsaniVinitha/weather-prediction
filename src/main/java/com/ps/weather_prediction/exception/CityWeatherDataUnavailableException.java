package com.ps.weather_prediction.exception;

public class CityWeatherDataUnavailableException extends RuntimeException {
    public CityWeatherDataUnavailableException(String message) {
        super(message);
    }

}
