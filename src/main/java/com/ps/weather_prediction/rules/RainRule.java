package com.ps.weather_prediction.rules;

import com.ps.weather_prediction.model.dto.WeatherApiResponse;

import java.util.List;

public class RainRule implements WeatherRule {
    public String apply(List<WeatherApiResponse.WeatherData> weatherDataList) {
        boolean hasRain = weatherDataList.stream()
                .anyMatch(weather -> weather.getRain()!= null && weather.getRain().getThreeHourRain() > 0);

        return hasRain ? "Carry umbrella" : null;
    }
}
