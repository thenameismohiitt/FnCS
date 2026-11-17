package com.fulfilment.application.monolith.fulfillment;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class FulfillmentRulesTest {

  @Test
  void allowsAnIndependentValidAssignment() {
    assertDoesNotThrow(() -> FulfillmentRules.validateNewAssignment(
        List.of(assignment(1, 1, 1)), assignment(1, 2, 2)));
  }

  @Test
  void rejectsDuplicateAndThirdWarehouseForAProductAtAStore() {
    var existing = List.of(assignment(1, 1, 1), assignment(1, 1, 2));

    assertThrows(IllegalArgumentException.class,
        () -> FulfillmentRules.validateNewAssignment(existing, assignment(1, 1, 1)));
    assertThrows(IllegalArgumentException.class,
        () -> FulfillmentRules.validateNewAssignment(existing, assignment(1, 1, 3)));
  }

  @Test
  void rejectsFourthDistinctWarehouseForAStoreButAllowsAnExistingOne() {
    var existing = List.of(assignment(1, 1, 1), assignment(1, 2, 2), assignment(1, 3, 3));

    assertThrows(IllegalArgumentException.class,
        () -> FulfillmentRules.validateNewAssignment(existing, assignment(1, 4, 4)));
    assertDoesNotThrow(() -> FulfillmentRules.validateNewAssignment(existing, assignment(1, 4, 1)));
  }

  @Test
  void rejectsSixthProductTypeForWarehouseButAllowsAnExistingType() {
    List<WarehouseFulfillment> existing = new ArrayList<>();
    for (long productId = 1; productId <= 5; productId++) {
      existing.add(assignment(productId, productId, 1));
    }

    assertThrows(IllegalArgumentException.class,
        () -> FulfillmentRules.validateNewAssignment(existing, assignment(6, 6, 1)));
    assertDoesNotThrow(() -> FulfillmentRules.validateNewAssignment(existing, assignment(6, 1, 1)));
  }

  private static WarehouseFulfillment assignment(long storeId, long productId, long warehouseId) {
    var assignment = new WarehouseFulfillment();
    assignment.storeId = storeId;
    assignment.productId = productId;
    assignment.warehouseId = warehouseId;
    return assignment;
  }
}
