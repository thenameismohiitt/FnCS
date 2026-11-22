package com.fulfilment.application.monolith.fulfillment;

import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class FulfillmentValidator {

  @Inject WarehouseFulfillmentRepository fulfillmentRepository;
  @Inject ProductRepository productRepository;
  @Inject WarehouseRepository warehouseRepository;

  public void validateForCreation(WarehouseFulfillment assignment) {
    validateRequest(assignment);
    validateReferences(assignment);
    try {
      FulfillmentRules.validateNewAssignment(fulfillmentRepository.listAll(), assignment);
    } catch (IllegalArgumentException exception) {
      throw new WebApplicationException(exception.getMessage(), Response.Status.CONFLICT);
    }
  }

  private void validateRequest(WarehouseFulfillment assignment) {
    if (assignment == null || assignment.id != null || assignment.storeId == null
        || assignment.productId == null || assignment.warehouseId == null) {
      throw new WebApplicationException(
          "storeId, productId and warehouseId are required; id must be omitted.",
          Response.Status.BAD_REQUEST);
    }
  }

  private void validateReferences(WarehouseFulfillment assignment) {
    if (Store.findById(assignment.storeId) == null) {
      throw new NotFoundException("Store with id " + assignment.storeId + " does not exist.");
    }
    if (productRepository.findById(assignment.productId) == null) {
      throw new NotFoundException("Product with id " + assignment.productId + " does not exist.");
    }
    if (warehouseRepository.findActiveById(assignment.warehouseId) == null) {
      throw new NotFoundException(
          "Active warehouse with id " + assignment.warehouseId + " does not exist.");
    }
  }
}
