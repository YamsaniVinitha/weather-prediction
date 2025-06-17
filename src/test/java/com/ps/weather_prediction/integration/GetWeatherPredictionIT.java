package com.ps.weather_prediction.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ps.weather_prediction.WeatherPredictionApplication;
import com.ps.weather_prediction.adapter.OpenWeatherAdapter;
import com.ps.weather_prediction.configuration.OpenWeatherApiConfiguration;
import com.ps.weather_prediction.controller.WeatherController;
import com.ps.weather_prediction.model.dto.ApplicationMode;
import com.ps.weather_prediction.model.dto.WeatherApiResponse;
import com.ps.weather_prediction.model.enums.Mode;
import com.ps.weather_prediction.model.response.WeatherDetails;
import com.ps.weather_prediction.model.response.WeatherResponse;
import com.ps.weather_prediction.service.WeatherCacheService;
import com.ps.weather_prediction.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {WeatherPredictionApplication.class})
@ActiveProfiles("test")
@Tag("Integration")
@Import(OpenWeatherApiConfiguration.class)
class GetWeatherPredictionIT {

    @Autowired
    WeatherController weatherController;

    @Autowired
    WeatherService weatherService;

    @MockitoBean
    OpenWeatherAdapter openWeatherAdapter;

    @Autowired
    WeatherCacheService weatherCacheService;

    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ApplicationMode applicationMode;

    MockMvc mockMvc;
    
    private static final String PARIS = "Paris";

    private static final String URI = "/weather";

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @Test
    void returns_weather_response_when_city_is_valid() throws Exception {
        //Given
        var weatherApiResponse = aWeatherApiResponse();
        when(openWeatherAdapter.getWeatherForecast(PARIS))
                .thenReturn(weatherApiResponse);

        // when
        ResultActions resultActions = performRequest(PARIS);

        // then
        resultActions.andExpect(status().isOk());
        String actual = resultActions.andReturn().getResponse().getContentAsString();
        var expected = aWeatherResponse();
        assertThat(objectMapper.readValue(actual, WeatherResponse.class)).isEqualTo(expected);
    }

    @Test
    void returns_weather_response_when_city_is_valid_and_cache_is_used() throws Exception {
        // Given

        var weatherApiResponse = aWeatherApiResponse();
        when(openWeatherAdapter.getWeatherForecast(PARIS))
                .thenReturn(weatherApiResponse);

        // when
        ResultActions resultActions = performRequest(PARIS);
        resultActions.andExpect(status().isOk());

        setApplicationModeToOffline();

        // when
        ResultActions resultActions1 = performRequest(PARIS);

        // then
        resultActions1.andExpect(status().isOk());
        String actual = resultActions1.andReturn().getResponse().getContentAsString();
        var expected = aWeatherResponse();
        assertThat(objectMapper.readValue(actual, WeatherResponse.class)).isEqualTo(expected);

        // when
        ResultActions resultActions2 = performRequest("Boston");
        // then
        resultActions2.andExpect(status().isInternalServerError());
    }


    private void setApplicationModeToOffline() {
        applicationMode.setMode(Mode.OFFLINE);
    }

    private ResultActions performRequest(String city) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(URI)
                .param("city", city));
    }

    private WeatherApiResponse aWeatherApiResponse() {
        var weatherData1 = WeatherApiResponse.WeatherData.builder()
                .dtTxt("2023-10-01")
                .main(WeatherApiResponse.WeatherData.Main.builder().temp(294.8).build())
                .rain(WeatherApiResponse.WeatherData.Rain.builder().threeHourRain(0.6).build())
                .build();
        var weatherData2 = WeatherApiResponse.WeatherData.builder()
                .dtTxt("2023-10-01")
                .main(WeatherApiResponse.WeatherData.Main.builder().temp(295.0).build())
                .build();
        var weatherData3 = WeatherApiResponse.WeatherData.builder()
                .dtTxt("2023-10-01")
                .main(WeatherApiResponse.WeatherData.Main.builder().temp(300).build())
                .build();
        var weatherData4 = WeatherApiResponse.WeatherData.builder()
                .dtTxt("2023-10-01")
                .main(WeatherApiResponse.WeatherData.Main.builder().temp(290).build())
                .build();
        var weatherData5 = WeatherApiResponse.WeatherData.builder()
                .dtTxt("2023-10-01")
                .main(WeatherApiResponse.WeatherData.Main.builder().temp(291).build())
                .build();
        var weatherData6 = WeatherApiResponse.WeatherData.builder()
                .dtTxt("2023-10-01")
                .main(WeatherApiResponse.WeatherData.Main.builder().temp(292).build())
                .build();
        var weatherData7 = WeatherApiResponse.WeatherData.builder()
                .dtTxt("2023-10-01")
                .main(WeatherApiResponse.WeatherData.Main.builder().temp(293).build())
                .build();
        var weatherData8 = WeatherApiResponse.WeatherData.builder()
                .dtTxt("2023-10-01")
                .main(WeatherApiResponse.WeatherData.Main.builder().temp(296).build())
                .build();
        var weatherDataList = List.of(weatherData1, weatherData2, weatherData3, weatherData4, weatherData5, weatherData6, weatherData7, weatherData8);

        return WeatherApiResponse.builder()
                .city(WeatherApiResponse.City.builder().name(PARIS).build())
                .list(weatherDataList)
                .build();
    }

    private WeatherResponse aWeatherResponse() {

        return WeatherResponse.builder()
                .city(PARIS)
                .weatherDetails(List.of(
                        WeatherDetails.builder()
                                .date("2023-10-01")
                                .temperature(WeatherDetails.Temperature.builder()
                                        .max(300)
                                        .min(290)
                                        .build())
                                .instruction("Carry umbrella")
                                .build()
                ))
                .build();
    }
}
