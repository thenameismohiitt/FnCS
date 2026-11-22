package com.fulfilment.application.monolith.fulfillment.application.ports;

import com.fulfilment.application.monolith.fulfillment.domain.model.FulfillmentAssignment;
import java.util.List;

public interface FulfillmentAssignmentStore {
  List<FulfillmentAssignment> listAll();
  void create(FulfillmentAssignment assignment);
  boolean deleteById(Long id);
}
