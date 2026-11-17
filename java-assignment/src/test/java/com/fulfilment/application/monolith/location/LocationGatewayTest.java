package com.fulfilment.application.monolith.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class LocationGatewayTest {

  @Test
  void resolvesExistingLocationIgnoringCase() {
    var location = new LocationGateway().resolveByIdentifier("zwolle-001");

    assertEquals("ZWOLLE-001", location.identification);
  }

  @Test
  void returnsNullForMissingOrNullIdentifier() {
    var locationGateway = new LocationGateway();

    assertNull(locationGateway.resolveByIdentifier("MISSING-001"));
    assertNull(locationGateway.resolveByIdentifier(null));
  }
}
