package com.trinity.common.dto;

import lombok.Data;

@Data
public class ConfigResponse {
    private final String name;
    private final String version;
}