package com.trinity.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trinity.common.config.AppConfig;
import com.trinity.common.dto.ConfigResponse;

import lombok.AllArgsConstructor;

/**
 * REST Controller to expose application configuration.
 */
@RestController
@RequestMapping("/api/v1/app")
@AllArgsConstructor
public class AppController {

    @Autowired
    private final AppConfig appConfig;

    /**
     * Endpoint to show configuration properties.
     */
    @GetMapping("/config")
    public ResponseEntity<ConfigResponse> showConfig() {
        return ResponseEntity.ok(new ConfigResponse(
            appConfig.getAppName(),
            appConfig.getAppVersion()
        ));
    }
}