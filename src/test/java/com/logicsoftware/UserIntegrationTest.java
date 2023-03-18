package com.logicsoftware;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import com.logicsoftware.dtos.user.UserDto;
import com.logicsoftware.keycloak.KeycloakLifecycle;
import com.logicsoftware.keycloak.KeycloakResource;
import com.logicsoftware.utils.request.PageResponse;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@QuarkusTestResource(KeycloakLifecycle.class)
public class UserIntegrationTest extends KeycloakResource {

    @Test
    public void userListEndpoint() {
        given()
            .auth().oauth2(getAccessToken("luis.eduardo", "123456"))
            .when()
            .contentType(ContentType.JSON)
            .queryParam("page", "1")
            .queryParam("size", "10")
            // .body(new UserFilterDto());
            .post("/users/list")
            .then()
            .statusCode(200)
            .extract()
            .body().as(PageResponse.class)
            .equals(
                PageResponse.<UserDto>builder()
                    .totalElements(0L)
                    .pageSize(10)
                    .totalPages(1)
                    .build()
            );
    }
}
