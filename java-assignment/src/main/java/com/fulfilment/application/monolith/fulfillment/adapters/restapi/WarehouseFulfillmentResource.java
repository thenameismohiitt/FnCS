package com.fulfilment.application.monolith.fulfillment.adapters.restapi;

import com.fulfilment.application.monolith.fulfillment.application.usecases.CreateFulfillmentAssignmentUseCase;
import com.fulfilment.application.monolith.fulfillment.application.usecases.DeleteFulfillmentAssignmentUseCase;
import com.fulfilment.application.monolith.fulfillment.application.usecases.ListFulfillmentAssignmentsUseCase;
import com.fulfilment.application.monolith.fulfillment.application.validator.InvalidFulfillmentException;
import com.fulfilment.application.monolith.fulfillment.application.validator.MissingReferenceException;
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

/** Inbound REST adapter for warehouse fulfilment assignments. */
@Path("fulfillment")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class WarehouseFulfillmentResource {
  @Inject CreateFulfillmentAssignmentUseCase createAssignment;
  @Inject ListFulfillmentAssignmentsUseCase listAssignments;
  @Inject DeleteFulfillmentAssignmentUseCase deleteAssignment;

  @GET
  public List<FulfillmentAssignmentDto> list() {
    return listAssignments.list().stream().map(FulfillmentAssignmentDto::from).toList();
  }

  @POST
  @Transactional
  @ResponseStatus(201)
  public FulfillmentAssignmentDto create(FulfillmentAssignmentDto assignment) {
    try {
      return FulfillmentAssignmentDto.from(createAssignment.create(FulfillmentAssignmentDto.toDomain(assignment)));
    } catch (MissingReferenceException exception) {
      throw new NotFoundException(exception.getMessage());
    } catch (InvalidFulfillmentException exception) {
      throw new WebApplicationException(exception.getMessage(), Response.Status.BAD_REQUEST);
    } catch (IllegalArgumentException exception) {
      throw new WebApplicationException(exception.getMessage(), Response.Status.CONFLICT);
    }
  }

  @DELETE
  @Path("{id}")
  @Transactional
  public Response delete(@PathParam("id") Long id) {
    if (!deleteAssignment.delete(id)) {
      throw new NotFoundException("Fulfillment assignment with id " + id + " does not exist.");
    }
    return Response.noContent().build();
  }
}
