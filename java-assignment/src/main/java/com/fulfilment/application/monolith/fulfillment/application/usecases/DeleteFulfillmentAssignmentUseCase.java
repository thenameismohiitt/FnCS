package com.fulfilment.application.monolith.fulfillment.application.usecases;

import com.fulfilment.application.monolith.fulfillment.application.ports.FulfillmentAssignmentStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DeleteFulfillmentAssignmentUseCase {
  private final FulfillmentAssignmentStore assignmentStore;
  @Inject
  public DeleteFulfillmentAssignmentUseCase(FulfillmentAssignmentStore assignmentStore) {
    this.assignmentStore = assignmentStore;
  }
  public boolean delete(Long id) { return assignmentStore.deleteById(id); }
}
