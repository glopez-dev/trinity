package com.trinity.common.interfaces.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.trinity.common.config.AppConfig;
import com.trinity.common.interfaces.rest.dto.ConfigResponse;

class AppControllerTest {

    @Mock
    private AppConfig appConfig;

    @InjectMocks
    private AppController appController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void showConfig_ReturnsOkWithConfigFromAppConfig() {
        when(appConfig.getAppName()).thenReturn("Trinity");
        when(appConfig.getAppVersion()).thenReturn("1.0.0");

        ResponseEntity<ConfigResponse> response = appController.showConfig();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Trinity", response.getBody().getName());
        assertEquals("1.0.0", response.getBody().getVersion());
    }
}
