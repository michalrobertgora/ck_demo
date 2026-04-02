package com.api.framework.service;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import static io.restassured.RestAssured.given;

/**
 * Abstract base for all API service classes.
 * Provides a simple way to make a call with API config defaults,
 * or to a builder to override the values.
 */
@Slf4j
public abstract class BaseService {

    private final RequestSpecification requestSpec;

    protected BaseService(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    /**
     * Returns a builder for per-request customisation.
     * Anything set on the builder overrides it for this call only.
     */
    protected RequestBuilder request() {
        return new RequestBuilder(requestSpec);
    }

    protected Response get(String path, Object... pathParams) {
        log.debug("GET {}", path);
        return given()
                .spec(requestSpec)
                .when()
                .get(path, pathParams);
    }

    protected Response getWithQueryParam(String path, String paramName, Object paramValue) {
        log.debug("GET {} ?{}={}", path, paramName, paramValue);
        return given()
                .spec(requestSpec)
                .queryParam(paramName, paramValue)
                .when()
                .get(path);
    }

    protected Response post(String path, Object body) {
        log.debug("POST {}", path);
        return given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post(path);
    }

    protected Response put(String path, Object body, Object... pathParams) {
        log.debug("PUT {}", path);
        return given()
                .spec(requestSpec)
                .body(body)
                .when()
                .put(path, pathParams);
    }

    protected Response patch(String path, Object body, Object... pathParams) {
        log.debug("PATCH {}", path);
        return given()
                .spec(requestSpec)
                .body(body)
                .when()
                .patch(path, pathParams);
    }

    protected Response delete(String path, Object... pathParams) {
        log.debug("DELETE {}", path);
        return given()
                .spec(requestSpec)
                .when()
                .delete(path, pathParams);
    }
}
