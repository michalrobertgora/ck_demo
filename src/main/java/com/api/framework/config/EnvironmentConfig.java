package com.api.framework.config;

import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Reads environment-specific properties and produces one {@link ApiConfig}
 * bean per API under test.
 *
 * The active Spring profile selects which {@code application-{env}.properties}
 * file is loaded.
 *
 * Many ways to skin a cat - if in real cases a lot of apps are expected to be added,
 * other less boilerplate-y ways to configure it could be applies.
 */
@Configuration
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class EnvironmentConfig {

    // --- CkAppAlpha ---------------------------------------------------------

    @Value("${api.ck-app-alpha.base-url}")
    private String ckAppAlphaBaseUrl;

    @Value("${api.ck-app-alpha.content-type:JSON}")
    private String ckAppAlphaContentType;

    @Value("${api.ck-app-alpha.timeout.connect:5000}")
    private int ckAppAlphaConnectTimeout;

    @Value("${api.ck-app-alpha.timeout.read:10000}")
    private int ckAppAlphaReadTimeout;

    @Value("${api.ck-app-alpha.auth.type:none}")
    private String ckAppAlphaAuthType;

    @Value("${api.ck-app-alpha.auth.token:}")
    private String ckAppAlphaAuthToken;

    @Value("${api.ck-app-alpha.logging:false}")
    private boolean ckAppAlphaLogging;

    // --- CkAppBeta ----------------------------------------------------------

    @Value("${api.ck-app-beta.base-url}")
    private String ckAppBetaBaseUrl;

    @Value("${api.ck-app-beta.content-type:JSON}")
    private String ckAppBetaContentType;

    @Value("${api.ck-app-beta.timeout.connect:5000}")
    private int ckAppBetaConnectTimeout;

    @Value("${api.ck-app-beta.timeout.read:10000}")
    private int ckAppBetaReadTimeout;

    @Value("${api.ck-app-beta.auth.type:none}")
    private String ckAppBetaAuthType;

    @Value("${api.ck-app-beta.auth.token:}")
    private String ckAppBetaAuthToken;

    @Value("${api.ck-app-beta.logging:false}")
    private boolean ckAppBetaLogging;

    // --- Bean factories ------------------------------------------------------

    @Bean
    @Qualifier("ckAppAlphaConfig")
    public ApiConfig ckAppAlphaConfig() {
        return ApiConfig.builder()
                .baseUrl(ckAppAlphaBaseUrl)
                .contentType(ContentType.valueOf(ckAppAlphaContentType))
                .connectTimeout(ckAppAlphaConnectTimeout)
                .readTimeout(ckAppAlphaReadTimeout)
                .authType(ckAppAlphaAuthType)
                .authToken(ckAppAlphaAuthToken)
                .loggingEnabled(ckAppAlphaLogging)
                .build();
    }

    @Bean
    @Qualifier("ckAppBetaConfig")
    public ApiConfig ckAppBetaConfig() {
        return ApiConfig.builder()
                .baseUrl(ckAppBetaBaseUrl)
                .contentType(ContentType.valueOf(ckAppBetaContentType))
                .connectTimeout(ckAppBetaConnectTimeout)
                .readTimeout(ckAppBetaReadTimeout)
                .authType(ckAppBetaAuthType)
                .authToken(ckAppBetaAuthToken)
                .loggingEnabled(ckAppBetaLogging)
                .build();
    }
}
