package com.fulfilment.application.monolith.fulfillment.application.usecases;

import com.fulfilment.application.monolith.fulfillment.application.ports.FulfillmentAssignmentStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DeleteFulfillmentAssignmentUseCase {
  @Inject FulfillmentAssignmentStore assignmentStore;
  public boolean delete(Long id) { return assignmentStore.deleteById(id); }
}
