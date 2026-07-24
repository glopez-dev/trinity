package com.trinity.common.interfaces.rest.dto;

import lombok.Data;

@Data
public class ConfigResponse {
    private final String name;
    private final String version;
}