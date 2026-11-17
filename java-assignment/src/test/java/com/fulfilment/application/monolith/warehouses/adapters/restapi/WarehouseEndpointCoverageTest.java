package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusTest
class WarehouseEndpointCoverageTest {
  @Test
  void exercisesWarehouseLifecycleAndValidation() {
    given().get("warehouse").then().statusCode(200);
    given().get("warehouse/2").then().statusCode(200);

    String payload = "{\"businessUnitCode\":\"MWH.COVERAGE\",\"location\":\"AMSTERDAM-001\",\"capacity\":10,\"stock\":3}";
    String id = given().contentType(ContentType.JSON).body(payload).post("warehouse")
        .then().statusCode(200).extract().path("id").toString();
    given().contentType(ContentType.JSON).body(payload).post("warehouse").then().statusCode(400);
    given().contentType(ContentType.JSON)
        .body("{\"businessUnitCode\":\"MWH.BAD\",\"location\":\"UNKNOWN\",\"capacity\":10,\"stock\":3}")
        .post("warehouse").then().statusCode(400);
    given().delete("warehouse/" + id).then().statusCode(204);
    given().get("warehouse/" + id).then().statusCode(404);

    given().contentType(ContentType.JSON)
        .body("{\"businessUnitCode\":\"MWH.023\",\"location\":\"AMSTERDAM-002\",\"capacity\":40,\"stock\":27}")
        .post("warehouse/MWH.023/replacement").then().statusCode(200);
  }
}
