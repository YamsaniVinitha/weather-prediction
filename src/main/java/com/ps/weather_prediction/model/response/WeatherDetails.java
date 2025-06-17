package com.ps.weather_prediction.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeatherDetails {
    String date;
    Temperature temperature;
    String instruction;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Temperature {
        double min;
        double max;
    }
}
