package com.fulfilment.application.monolith.fulfillment.application.usecases;

import com.fulfilment.application.monolith.fulfillment.application.ports.FulfillmentAssignmentStore;
import com.fulfilment.application.monolith.fulfillment.domain.model.FulfillmentAssignment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ListFulfillmentAssignmentsUseCase {
  @Inject FulfillmentAssignmentStore assignmentStore;
  public List<FulfillmentAssignment> list() { return assignmentStore.listAll(); }
}
