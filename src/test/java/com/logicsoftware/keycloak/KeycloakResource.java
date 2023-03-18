package com.logicsoftware.keycloak;

import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.representations.AccessTokenResponse;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public abstract class KeycloakResource {

    @ConfigProperty(name = "quarkus.oidc.auth-server-url")
    String authServerUrl;

    @ConfigProperty(name = "quarkus.oidc.client-id")
    String clientId;

    @ConfigProperty(name = "quarkus.oidc.credentials.secret")
    String clientSecret;

    public String getAccessToken(String username, String password) {
        return createRequestSpec()
                .contentType(ContentType.URLENC)
                .formParams(Map.of(
                    "username", username,
                    "password", password,
                    "grant_type", "password",
                    "client_id", clientId,
                    "client_secret", clientSecret
                ))
                .when()
                .post(authServerUrl + "/protocol/openid-connect/token")
                .as(AccessTokenResponse.class).getToken();
    }

    private static RequestSpecification createRequestSpec() {
        return RestAssured.given();
    }
}
