package com.api.framework.service;

import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Request builder example - over-engineered for the sake of demo.
 */
public class RequestBuilder {

    private final RequestSpecification baseSpec;
    private final Map<String, String> headers = new LinkedHashMap<>();
    private final Map<String, Object> queryParams = new LinkedHashMap<>();
    private ContentType contentType;
    private Integer expectedStatus;
    private boolean logRequest;
    private boolean logResponse;

    RequestBuilder(RequestSpecification baseSpec) {
        this.baseSpec = baseSpec;
    }

    public RequestBuilder header(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public RequestBuilder headers(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    // Add params

    public RequestBuilder queryParam(String name, Object value) {
        queryParams.put(name, value);
        return this;
    }

    // Content type

    public RequestBuilder contentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    // Custom status

    public RequestBuilder expectStatus(int expectedStatus) {
        this.expectedStatus = expectedStatus;
        return this;
    }

    // Logging

    public RequestBuilder withLogging() {
        this.logRequest = true;
        this.logResponse = true;
        return this;
    }

    // HTTP methods

    public Response get(String path, Object... pathParams) {
        return execute("GET", path, null, pathParams);
    }

    public Response post(String path, Object body) {
        return execute("POST", path, body);
    }

    public Response put(String path, Object body, Object... pathParams) {
        return execute("PUT", path, body, pathParams);
    }

    public Response patch(String path, Object body, Object... pathParams) {
        return execute("PATCH", path, body, pathParams);
    }

    public Response delete(String path, Object... pathParams) {
        return execute("DELETE", path, null, pathParams);
    }


    private Response execute(String method, String path, Object body, Object... pathParams) {
        RequestSpecification spec = given().spec(baseSpec);

        if (!headers.isEmpty()) {
            spec.headers(headers);
        }
        if (!queryParams.isEmpty()) {
            queryParams.forEach(spec::queryParam);
        }
        if (contentType != null) {
            spec.contentType(contentType);
        }
        if (logRequest) {
            spec.filter(new RequestLoggingFilter(LogDetail.ALL));
        }
        if (logResponse) {
            spec.filter(new ResponseLoggingFilter(LogDetail.ALL));
        }
        if (body != null) {
            spec.body(body);
        }

        Response response = switch (method) {
            case "GET"    -> spec.when().get(path, pathParams);
            case "POST"   -> spec.when().post(path, pathParams);
            case "PUT"    -> spec.when().put(path, pathParams);
            case "PATCH"  -> spec.when().patch(path, pathParams);
            case "DELETE" -> spec.when().delete(path, pathParams);
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        };

        if (expectedStatus != null) {
            response.then().statusCode(expectedStatus);
        }

        return response;
    }
}
