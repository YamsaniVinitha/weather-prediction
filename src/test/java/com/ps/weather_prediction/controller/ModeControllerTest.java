package com.ps.weather_prediction.controller;

import com.ps.weather_prediction.exception.ExceptionHandlingController;
import com.ps.weather_prediction.model.dto.ApplicationMode;
import com.ps.weather_prediction.model.enums.Mode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ModeControllerTest {

    @Mock
    private ApplicationMode applicationMode;

    @InjectMocks
    private ModeController modeController;

    MockMvc mockmvc;

    @BeforeEach
    void setUp() {
        mockmvc = MockMvcBuilders.standaloneSetup(modeController)
                .setControllerAdvice(new ExceptionHandlingController())
                .build();
    }

    @Test
    void setMode_shouldSetApplicationMode() throws Exception {

        // When & Then
        mockmvc.perform(MockMvcRequestBuilders.post("/mode/ONLINE"))
                .andExpect(status().isOk());
    }

    @Test
    void setMode_throwsException_whenInvalidMode() throws Exception {
        // Given
        String invalidMode = "INVALID_MODE";

        // When & Then
        mockmvc.perform(MockMvcRequestBuilders.post("/mode/" + invalidMode))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMode_shouldReturnCurrentMode() throws Exception {
        // Given
        when(applicationMode.getMode()).thenReturn(Mode.ONLINE);

        // When & Then
        mockmvc.perform(MockMvcRequestBuilders.get("/mode"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.equals("ONLINE");
                });
    }

}