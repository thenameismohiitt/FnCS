package com.fulfilment.application.monolith.fulfillment;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusTest
class WarehouseFulfillmentEndpointTest {
  @Test
  void createsListsAndDeletesFulfillmentAssignment() {
    String payload = "{\"storeId\":2,\"productId\":2,\"warehouseId\":2}";
    String id = given().contentType(ContentType.JSON).body(payload).post("fulfillment")
        .then().statusCode(201).extract().path("id").toString();
    given().get("fulfillment").then().statusCode(200);
    given().contentType(ContentType.JSON).body(payload).post("fulfillment").then().statusCode(409);
    given().contentType(ContentType.JSON)
        .body("{\"storeId\":2,\"productId\":2,\"warehouseId\":99999}")
        .post("fulfillment").then().statusCode(404);
    given().delete("fulfillment/" + id).then().statusCode(204);
    given().delete("fulfillment/" + id).then().statusCode(404);
  }
}
