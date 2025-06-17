package com.ps.weather_prediction.service;

import com.ps.weather_prediction.exception.CityWeatherDataUnavailableException;
import com.ps.weather_prediction.model.dto.WeatherApiResponse;
import com.ps.weather_prediction.model.dto.ApplicationMode;
import com.ps.weather_prediction.model.enums.Mode;
import com.ps.weather_prediction.model.response.WeatherDetails;
import com.ps.weather_prediction.model.response.WeatherResponse;
import com.ps.weather_prediction.rules.HeatRule;
import com.ps.weather_prediction.rules.RainRule;
import com.ps.weather_prediction.rules.WeatherRule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherCacheService weatherCacheService;
    private final ApplicationMode applicationMode;
    private final LiveWeatherService liveWeatherService;

    @Value("${weather.api.days}")
    private int days;

    public WeatherResponse getWeatherPrediction(String cityName) {
        WeatherApiResponse apiResponse;
        if (Mode.ONLINE.equals(applicationMode.getMode())) {
            apiResponse = liveWeatherService.getAndCacheWeatherPrediction(cityName);
        } else {
            apiResponse = weatherCacheService.getCachedWeatherPrediction(cityName);
            if (apiResponse == null) {
                throw new CityWeatherDataUnavailableException("Weather data unavailable for city: " + cityName + ". Please try again later.");
            }
        }
        return buildWeatherResponseForApiResponse(apiResponse);
    }

    private WeatherResponse buildWeatherResponseForApiResponse(WeatherApiResponse apiResponse) {
        Map<String, List<WeatherApiResponse.WeatherData>> dateToWeatherDataListMap =
                getDateToWeatherDataListMap(apiResponse.getList(), days);
        return buildWeatherResponse(dateToWeatherDataListMap, apiResponse.getCity().getName());
    }

    private WeatherResponse buildWeatherResponse(Map<String, List<WeatherApiResponse.WeatherData>> dateToWeatherDataListMap, String cityName) {

        List<WeatherDetails> weatherDetailsList = new ArrayList<>();
        dateToWeatherDataListMap.forEach((date, weatherDataListForDate) -> {

            var temperatureList = weatherDataListForDate.stream()
                    .mapToDouble(weatherData -> weatherData.getMain().getTemp())
                    .toArray();
            var temperature = WeatherDetails.Temperature.builder()
                    .max(Arrays.stream(temperatureList).max().orElse(0))
                    .min(Arrays.stream(temperatureList).min().orElse(0))
                    .build();

            List<WeatherRule> weatherRules = List.of(new HeatRule(), new RainRule());
            var instructions = weatherRules.stream()
                    .map(weatherRule -> weatherRule.apply(weatherDataListForDate))
                    .filter(Objects::nonNull)
                    .findFirst().orElse(null);

            weatherDetailsList.add(WeatherDetails.builder()
                    .date(date)
                    .temperature(temperature)
                    .instruction(instructions)
                    .build());
        });
        return WeatherResponse.builder()
                .city(cityName)
                .weatherDetails(weatherDetailsList)
                .build();
    }

    private static Map<String, List<WeatherApiResponse.WeatherData>> getDateToWeatherDataListMap(List<WeatherApiResponse.WeatherData> weatherDataList, int days) {
        return weatherDataList.stream()
                .collect(Collectors.groupingBy(weatherData -> weatherData.getDtTxt().substring(0, 10)))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() == 8) // filter out today's weather info
                .limit(days)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
