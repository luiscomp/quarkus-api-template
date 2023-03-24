package com.logicsoftware.v1.services.user;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.logicsoftware.database.DatabaseLifecycle;
import com.logicsoftware.dtos.user.UserDto;
import com.logicsoftware.keycloak.KeycloakResource;
import com.logicsoftware.utils.request.PageResponse;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@DBRider
@DBUnit(caseSensitiveTableNames = true)
@QuarkusTest
@QuarkusTestResource(value = DatabaseLifecycle.class, parallel = true)
public class UserIntegrationTestV1 extends KeycloakResource {

    @Test
    @DataSet("users.yml")
    public void userListEndpoint() {
        given()
            .auth().oauth2(getAccessToken("luis.eduardo", "123456"))
            .when()
            .contentType(ContentType.JSON)
            .queryParam("page", "1")
            .queryParam("size", "10")
            .post("/user/v1/list")
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
