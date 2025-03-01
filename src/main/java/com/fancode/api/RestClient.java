package com.fancode.api;

import com.fancode.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RestClient {
    private static final Logger logger = LogManager.getLogger(RestClient.class);
    private final RequestSpecification requestSpec;

    public RestClient() {
        ConfigManager configManager = ConfigManager.getInstance();
        String baseUrl = configManager.getProperty("base.url");

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .build();

        logger.info("Initialized REST client with base URL: {}", baseUrl);
    }

    public Response get(String endpoint) {
        logger.info("Sending GET request to: {}", endpoint);
        Response response = RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
        logResponse(response);
        return response;
    }

    private void logResponse(Response response) {
        logger.info("Response Status Code: {}", response.getStatusCode());
        logger.debug("Response Body: {}", response.getBody().asString());
    }
}