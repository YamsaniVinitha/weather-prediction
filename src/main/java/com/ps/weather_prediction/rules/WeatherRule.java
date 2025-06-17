package com.ps.weather_prediction.rules;

import com.ps.weather_prediction.model.dto.WeatherApiResponse;

import java.util.List;

public interface WeatherRule {
    String apply(List<WeatherApiResponse.WeatherData> weatherDataList);
}
