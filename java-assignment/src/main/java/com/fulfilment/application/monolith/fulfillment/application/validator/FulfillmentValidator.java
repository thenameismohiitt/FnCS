package com.fulfilment.application.monolith.fulfillment.application.validator;

import com.fulfilment.application.monolith.fulfillment.application.ports.FulfillmentAssignmentStore;
import com.fulfilment.application.monolith.fulfillment.application.ports.FulfillmentReferenceCatalog;
import com.fulfilment.application.monolith.fulfillment.domain.model.FulfillmentAssignment;
import com.fulfilment.application.monolith.fulfillment.domain.service.FulfillmentPolicy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/** Application validation for incoming fulfilment assignments. */
@ApplicationScoped
public class FulfillmentValidator {
  private final FulfillmentAssignmentStore assignmentStore;
  private final FulfillmentReferenceCatalog references;

  @Inject
  public FulfillmentValidator(FulfillmentAssignmentStore assignmentStore,
      FulfillmentReferenceCatalog references) {
    this.assignmentStore = assignmentStore;
    this.references = references;
  }

  public void validateForCreation(FulfillmentAssignment assignment) {
    if (assignment == null || assignment.id != null || assignment.storeId == null
        || assignment.productId == null || assignment.warehouseId == null) {
      throw new InvalidFulfillmentException("storeId, productId and warehouseId are required; id must be omitted.");
    }
    if (!references.storeExists(assignment.storeId)) throw new MissingReferenceException("Store", assignment.storeId);
    if (!references.productExists(assignment.productId)) throw new MissingReferenceException("Product", assignment.productId);
    if (!references.activeWarehouseExists(assignment.warehouseId)) throw new MissingReferenceException("Active warehouse", assignment.warehouseId);
    FulfillmentPolicy.validateNewAssignment(assignmentStore.listAll(), assignment);
  }
}
