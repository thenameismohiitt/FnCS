package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fulfilment.application.monolith.location.LocationGateway;
import org.junit.jupiter.api.Test;

class ReplaceWarehouseUseCaseTest {

  @Test
  void archivesPreviousWarehouseAndCreatesReplacement() {
    var store = new InMemoryWarehouseStore();
    var previous = CreateWarehouseUseCaseTest.warehouse("MWH.001", "ZWOLLE-001", 20, 10);
    store.create(previous);
    var replacement = CreateWarehouseUseCaseTest.warehouse("MWH.001", "AMSTERDAM-001", 30, 10);

    new ReplaceWarehouseUseCase(store, new LocationGateway()).replace(replacement);

    assertNotNull(previous.archivedAt);
    assertNotNull(replacement.id);
    assertEquals(1, store.getAll().size());
    assertEquals(replacement, store.findByBusinessUnitCode("MWH.001"));
  }

  @Test
  void rejectsAReplacementWithDifferentStockOrInsufficientCapacity() {
    var store = new InMemoryWarehouseStore();
    store.create(CreateWarehouseUseCaseTest.warehouse("MWH.001", "ZWOLLE-001", 20, 10));
    var useCase = new ReplaceWarehouseUseCase(store, new LocationGateway());

    assertThrows(IllegalArgumentException.class,
        () -> useCase.replace(CreateWarehouseUseCaseTest.warehouse("MWH.001", "AMSTERDAM-001", 30, 9)));
    assertThrows(IllegalArgumentException.class,
        () -> useCase.replace(CreateWarehouseUseCaseTest.warehouse("MWH.001", "AMSTERDAM-001", 5, 10)));
  }
}
