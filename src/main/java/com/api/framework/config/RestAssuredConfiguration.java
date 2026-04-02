package com.api.framework.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.LogConfig;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Builds RequestSpecification per API from its ApiConfig.
 */
@Configuration
public class RestAssuredConfiguration {

    @Bean
    @Qualifier("ckAppAlpha")
    public RequestSpecification ckAppAlphaSpec(
            @Qualifier("ckAppAlphaConfig") ApiConfig config) {
        return buildSpec(config);
    }

    @Bean
    @Qualifier("ckAppBeta")
    public RequestSpecification ckAppBetaSpec(
            @Qualifier("ckAppBetaConfig") ApiConfig config) {
        return buildSpec(config);
    }

    private RequestSpecification buildSpec(ApiConfig config) {
        RestAssuredConfig raConfig = RestAssuredConfig.config()
                .objectMapperConfig(ObjectMapperConfig.objectMapperConfig()
                        .defaultObjectMapperType(ObjectMapperType.JACKSON_2))
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", config.getConnectTimeout())
                        .setParam("http.socket.timeout", config.getReadTimeout()))
                .logConfig(LogConfig.logConfig()
                        .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL));

        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(config.getBaseUrl())
                .setContentType(config.getContentType())
                .setAccept(config.getContentType())
                .setConfig(raConfig);

        applyAuth(builder, config);

        if (config.isLoggingEnabled()) {
            builder.addFilter(new RequestLoggingFilter(LogDetail.ALL));
            builder.addFilter(new ResponseLoggingFilter(LogDetail.ALL));
        }

        return builder.build();
    }

    private void applyAuth(RequestSpecBuilder builder, ApiConfig config) {
        if ("bearer".equalsIgnoreCase(config.getAuthType())
                && config.getAuthToken() != null
                && !config.getAuthToken().isBlank()) {
            builder.addHeader("Authorization", "Bearer " + config.getAuthToken());
        }
    }
}
