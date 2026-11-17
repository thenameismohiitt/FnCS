package com.fulfilment.application.monolith.stores;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusTest
class StoreEndpointCoverageTest {
  @Test
  void exercisesStoreCrudAndValidation() {
    String id = given().contentType(ContentType.JSON).body("{\"name\":\"COVERAGE-STORE\",\"quantityProductsInStock\":4}")
        .post("store").then().statusCode(201).extract().path("id").toString();
    given().get("store/" + id).then().statusCode(200);
    given().contentType(ContentType.JSON).body("{\"name\":\"COVERAGE-STORE-2\",\"quantityProductsInStock\":5}")
        .put("store/" + id).then().statusCode(200);
    given().contentType(ContentType.JSON).body("{\"name\":\"COVERAGE-STORE-3\",\"quantityProductsInStock\":6}")
        .patch("store/" + id).then().statusCode(200);
    given().get("store/99999").then().statusCode(404);
    given().contentType(ContentType.JSON).body("{\"quantityProductsInStock\":1}")
        .put("store/" + id).then().statusCode(422);
    given().delete("store/" + id).then().statusCode(204);
  }
}
