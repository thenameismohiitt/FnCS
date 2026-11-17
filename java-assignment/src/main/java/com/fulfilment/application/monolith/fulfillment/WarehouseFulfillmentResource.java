package com.fulfilment.application.monolith.fulfillment;

import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.jboss.resteasy.reactive.ResponseStatus;

/** HTTP API for assigning warehouses as product fulfilment units for stores. */
@Path("fulfillment")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class WarehouseFulfillmentResource {

  @Inject WarehouseFulfillmentRepository fulfillmentRepository;
  @Inject ProductRepository productRepository;
  @Inject WarehouseRepository warehouseRepository;

  @GET
  public List<WarehouseFulfillment> list() {
    return fulfillmentRepository.listAll();
  }

  @POST
  @Transactional
  @ResponseStatus(201)
  public WarehouseFulfillment create(WarehouseFulfillment assignment) {
    validateRequest(assignment);
    ensureReferencesExist(assignment);

    try {
      FulfillmentRules.validateNewAssignment(fulfillmentRepository.listAll(), assignment);
    } catch (IllegalArgumentException exception) {
      throw conflict(exception.getMessage());
    }

    fulfillmentRepository.persist(assignment);
    return assignment;
  }

  @DELETE
  @Path("{id}")
  @Transactional
  public Response delete(@PathParam("id") Long id) {
    if (!fulfillmentRepository.deleteById(id)) {
      throw new NotFoundException("Fulfillment assignment with id " + id + " does not exist.");
    }
    return Response.noContent().build();
  }

  private void validateRequest(WarehouseFulfillment assignment) {
    if (assignment == null || assignment.id != null || assignment.storeId == null
        || assignment.productId == null || assignment.warehouseId == null) {
      throw new WebApplicationException(
          "storeId, productId and warehouseId are required; id must be omitted.", Response.Status.BAD_REQUEST);
    }
  }

  private void ensureReferencesExist(WarehouseFulfillment assignment) {
    if (Store.findById(assignment.storeId) == null) {
      throw new NotFoundException("Store with id " + assignment.storeId + " does not exist.");
    }
    if (productRepository.findById(assignment.productId) == null) {
      throw new NotFoundException("Product with id " + assignment.productId + " does not exist.");
    }
    if (warehouseRepository.findActiveById(assignment.warehouseId) == null) {
      throw new NotFoundException("Active warehouse with id " + assignment.warehouseId + " does not exist.");
    }
  }

  private WebApplicationException conflict(String message) {
    return new WebApplicationException(message, Response.Status.CONFLICT);
  }
}
