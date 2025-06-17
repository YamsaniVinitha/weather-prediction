package com.ps.weather_prediction.controller;

import com.ps.weather_prediction.exception.CityNotFoundException;
import com.ps.weather_prediction.exception.ExceptionHandlingController;
import com.ps.weather_prediction.model.response.WeatherResponse;
import com.ps.weather_prediction.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class WeatherControllerTest {

    @InjectMocks
    private WeatherController weatherController;

    @Mock
    private WeatherService weatherService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController)
                .setControllerAdvice(new ExceptionHandlingController())
                .build();
    }

    @Test
    void getWeatherForecast_ShouldReturnWeatherData() throws Exception {
        var weatherResponse = WeatherResponse.builder().city("Paris").build();
        when(weatherService.getWeatherPrediction("Paris")).thenReturn(weatherResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/weather")
                        .param("city", "Paris"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value("Paris"));
    }

    @Test
    void getWeatherForecast_ShouldReturnBadRequest_WhenCityIsMissing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/weather"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required request parameter 'city' for method parameter type String is not present"));
    }

    @Test
    void getWeatherForecast_ShouldReturnNotFound_WhenCityDoesNotExist() throws Exception {
        when(weatherService.getWeatherPrediction("UnknownCity"))
                .thenThrow(new CityNotFoundException("City not found: UnknownCity"));

        mockMvc.perform(MockMvcRequestBuilders.get("/weather")
                        .param("city", "UnknownCity"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("City not found: UnknownCity"));
    }

    @Test
    void getWeatherForecast_ShouldReturnInternalServerError_WhenServiceThrowsException() throws Exception {
        when(weatherService.getWeatherPrediction("Paris"))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(MockMvcRequestBuilders.get("/weather")
                        .param("city", "Paris"))
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("An unexpected error occurred: Unexpected error"));
    }
}