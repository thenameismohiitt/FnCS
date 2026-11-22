package com.fulfilment.application.monolith.fulfillment.adapters.database;

import com.fulfilment.application.monolith.fulfillment.application.ports.FulfillmentAssignmentStore;
import com.fulfilment.application.monolith.fulfillment.domain.model.FulfillmentAssignment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class FulfillmentAssignmentRepository implements FulfillmentAssignmentStore {
  @Inject DbFulfillmentAssignmentRepository repository;

  @Override
  public List<FulfillmentAssignment> listAll() {
    return repository.listAll().stream().map(DbFulfillmentAssignment::toDomain).toList();
  }

  @Override
  public void create(FulfillmentAssignment assignment) {
    DbFulfillmentAssignment entity = DbFulfillmentAssignment.from(assignment);
    repository.persist(entity);
    assignment.id = entity.id;
  }

  @Override
  public boolean deleteById(Long id) {
    return id != null && repository.deleteById(id);
  }
}
