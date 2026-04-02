package com.api.framework.config;

import io.restassured.http.ContentType;
import lombok.Builder;
import lombok.Getter;

/**
 * Typed per-API configuration.
 *
 * One instance per API under test. Holds everything needed to build
 * a io.restassured.specification.RequestSpecification.
 * Adding a new API: add new ApiConfig bean in EnvironmentConfig.
 */
@Getter
@Builder
public class ApiConfig {

    private final String baseUrl;
    private final ContentType contentType;
    private final int connectTimeout;
    private final int readTimeout;
    private final String authType;
    private final String authToken;
    private final boolean loggingEnabled;
}
