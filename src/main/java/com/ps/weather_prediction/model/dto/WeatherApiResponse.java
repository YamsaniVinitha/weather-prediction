package com.ps.weather_prediction.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
@Builder(toBuilder = true)
public class WeatherApiResponse {
    String cod;
    int message;
    int cnt;
    List<WeatherData> list;
    City city;

    @Value
    @Builder(toBuilder = true)
    public static class WeatherData {
        long dt;
        Main main;
        List<Weather> weather;
        Clouds clouds;
        Wind wind;
        double visibility;
        double pop;
        Rain rain;
        Sys sys;
        @JsonProperty("dt_txt")
        String dtTxt;


        @Value
        @Builder
        public static class Main {
            double temp;
            @JsonProperty("feels_like")
            double feelsLike;
            @JsonProperty("temp_min")
            double tempMin;
            @JsonProperty("temp_max")
            double tempMax;
            int pressure;
            @JsonProperty("sea_level")
            int seaLevel;
            @JsonProperty("grnd_level")
            int grndLevel;
            int humidity;
            @JsonProperty("temp_kf")
            int tempKf;
        }

        @Value
        public static class Weather {
            int id;
            String main;
            String description;
            String icon;
        }

        @Value
        public static class Clouds {
            int all;
        }

        @Value
        public static class Wind {
            double speed;
            int deg;
            double gust;
        }

        @Value
        @Builder
        public static class Rain {
            @JsonProperty("3h")
            double threeHourRain;
        }

        @Value
        public static class Sys {
            String pod;
        }
    }

    @Value
    @Builder(toBuilder = true)
    public static class City {
        int id;
        String name;
        Coord coord;
        String country;
        int population;
        int timezone;
        long sunrise;
        long sunset;

        @Value
        public static class Coord {
            double lat;
            double lon;
        }
    }
}