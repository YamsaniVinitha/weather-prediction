package com.ps.weather_prediction.rules;

import com.ps.weather_prediction.model.dto.WeatherApiResponse;

import java.util.List;

public class HeatRule implements WeatherRule {
    public String apply(List<WeatherApiResponse.WeatherData> weatherDataList) {
        boolean hasRain = weatherDataList.stream()
                .anyMatch(weather -> weather.getMain().getTemp() > 313.15); // 40°C in Kelvin

        return hasRain ? "Use sunscreen lotion" : null;
    }
}
