package com.fulfilment.application.monolith.fulfillment;

import com.fulfilment.application.monolith.fulfillment.domain.service.FulfillmentPolicy;
import java.util.List;

@Deprecated
final class FulfillmentRules {
  private FulfillmentRules() {}
  static void validateNewAssignment(List<WarehouseFulfillment> assignments, WarehouseFulfillment candidate) {
    FulfillmentPolicy.validateNewAssignment(assignments, candidate);
  }
}
