package com.logicsoftware;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import com.logicsoftware.dtos.user.UserDto;
import com.logicsoftware.dtos.user.UserFilterDto;
import com.logicsoftware.utils.request.PageResponse;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class UserResourceTest {

    @Test
    public void userListEndpoint() {
        given()
            .when()
            .contentType(ContentType.JSON)
            .queryParam("page", 1)
            .queryParam("size", 10)
            .body(new UserFilterDto())
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