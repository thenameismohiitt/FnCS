package com.fulfilment.application.monolith.fulfillment.application;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fulfilment.application.monolith.fulfillment.application.ports.FulfillmentAssignmentStore;
import com.fulfilment.application.monolith.fulfillment.application.ports.FulfillmentReferenceCatalog;
import com.fulfilment.application.monolith.fulfillment.application.usecases.CreateFulfillmentAssignmentUseCase;
import com.fulfilment.application.monolith.fulfillment.application.usecases.DeleteFulfillmentAssignmentUseCase;
import com.fulfilment.application.monolith.fulfillment.application.usecases.ListFulfillmentAssignmentsUseCase;
import com.fulfilment.application.monolith.fulfillment.application.validator.FulfillmentValidator;
import com.fulfilment.application.monolith.fulfillment.application.validator.InvalidFulfillmentException;
import com.fulfilment.application.monolith.fulfillment.application.validator.MissingReferenceException;
import com.fulfilment.application.monolith.fulfillment.domain.model.FulfillmentAssignment;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class FulfillmentApplicationTest {

  @Test
  void validatorAcceptsValidAssignmentAndRejectsInvalidRequests() {
    InMemoryStore store = new InMemoryStore();
    FulfillmentValidator validator = validator(store, references(true, true, true));

    assertDoesNotThrow(() -> validator.validateForCreation(assignment(1, 1, 1)));
    assertThrows(InvalidFulfillmentException.class, () -> validator.validateForCreation(null));
    FulfillmentAssignment withId = assignment(1, 1, 1);
    withId.id = 5L;
    assertThrows(InvalidFulfillmentException.class, () -> validator.validateForCreation(withId));
  }

  @Test
  void validatorRejectsMissingReferencesAndPolicyViolations() {
    InMemoryStore store = new InMemoryStore();
    assertThrows(MissingReferenceException.class,
        () -> validator(store, references(false, true, true)).validateForCreation(assignment(1, 1, 1)));
    assertThrows(MissingReferenceException.class,
        () -> validator(store, references(true, false, true)).validateForCreation(assignment(1, 1, 1)));
    assertThrows(MissingReferenceException.class,
        () -> validator(store, references(true, true, false)).validateForCreation(assignment(1, 1, 1)));

    store.create(assignment(1, 1, 1));
    assertThrows(IllegalArgumentException.class,
        () -> validator(store, references(true, true, true)).validateForCreation(assignment(1, 1, 1)));
  }

  @Test
  void useCasesCreateListAndDeleteAssignments() {
    InMemoryStore store = new InMemoryStore();
    CreateFulfillmentAssignmentUseCase create = new CreateFulfillmentAssignmentUseCase(
        store, validator(store, references(true, true, true)));
    FulfillmentAssignment created = create.create(assignment(2, 3, 4));

    assertEquals(1L, created.id);
    assertEquals(List.of(created), new ListFulfillmentAssignmentsUseCase(store).list());
    assertTrue(new DeleteFulfillmentAssignmentUseCase(store).delete(created.id));
    assertFalse(new DeleteFulfillmentAssignmentUseCase(store).delete(created.id));
  }

  private static FulfillmentValidator validator(
      FulfillmentAssignmentStore store, FulfillmentReferenceCatalog references) {
    return new FulfillmentValidator(store, references);
  }

  private static FulfillmentReferenceCatalog references(
      boolean store, boolean product, boolean warehouse) {
    return new FulfillmentReferenceCatalog() {
      @Override public boolean storeExists(Long id) { return store; }
      @Override public boolean productExists(Long id) { return product; }
      @Override public boolean activeWarehouseExists(Long id) { return warehouse; }
    };
  }

  private static FulfillmentAssignment assignment(long storeId, long productId, long warehouseId) {
    FulfillmentAssignment assignment = new FulfillmentAssignment();
    assignment.storeId = storeId;
    assignment.productId = productId;
    assignment.warehouseId = warehouseId;
    return assignment;
  }

  private static final class InMemoryStore implements FulfillmentAssignmentStore {
    private final List<FulfillmentAssignment> assignments = new ArrayList<>();
    private long nextId = 1;

    @Override public List<FulfillmentAssignment> listAll() { return List.copyOf(assignments); }
    @Override public void create(FulfillmentAssignment assignment) {
      assignment.id = nextId++;
      assignments.add(assignment);
    }
    @Override public boolean deleteById(Long id) {
      return assignments.removeIf(assignment -> assignment.id.equals(id));
    }
  }
}
