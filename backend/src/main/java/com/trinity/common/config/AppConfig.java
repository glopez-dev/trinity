package com.trinity.common.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration class to access application properties.
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppConfig  { 

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Value("${app.name}") // Injects 'app.name' property
    private String appName;

    @Value("${app.version}") // Injects 'app.version' property
    private String appVersion;

    public void printConfig() {
        logger.info("Application Name: " + appName);
        logger.info("Application Version: %s" + appVersion);
    }
}
