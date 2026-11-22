package com.fulfilment.application.monolith.fulfillment.application.usecases;

import com.fulfilment.application.monolith.fulfillment.application.ports.FulfillmentAssignmentStore;
import com.fulfilment.application.monolith.fulfillment.application.validator.FulfillmentValidator;
import com.fulfilment.application.monolith.fulfillment.domain.model.FulfillmentAssignment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateFulfillmentAssignmentUseCase {
  private final FulfillmentAssignmentStore assignmentStore;
  private final FulfillmentValidator validator;

  @Inject
  public CreateFulfillmentAssignmentUseCase(
      FulfillmentAssignmentStore assignmentStore, FulfillmentValidator validator) {
    this.assignmentStore = assignmentStore;
    this.validator = validator;
  }

  public FulfillmentAssignment create(FulfillmentAssignment assignment) {
    validator.validateForCreation(assignment);
    assignmentStore.create(assignment);
    return assignment;
  }
}
