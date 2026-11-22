package com.fulfilment.application.monolith.fulfillment;

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
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.jboss.resteasy.reactive.ResponseStatus;

@Path("fulfillment")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class WarehouseFulfillmentResource {

  @Inject WarehouseFulfillmentRepository fulfillmentRepository;
  @Inject FulfillmentValidator fulfillmentValidator;

  @GET
  public List<WarehouseFulfillment> list() {
    return fulfillmentRepository.listAll();
  }

  @POST
  @Transactional
  @ResponseStatus(201)
  public WarehouseFulfillment create(WarehouseFulfillment assignment) {
    fulfillmentValidator.validateForCreation(assignment);

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
}
