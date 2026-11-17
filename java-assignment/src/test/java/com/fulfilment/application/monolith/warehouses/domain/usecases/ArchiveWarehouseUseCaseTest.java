package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ArchiveWarehouseUseCaseTest {

  @Test
  void archivesAnActiveWarehouse() {
    var store = new InMemoryWarehouseStore();
    Warehouse warehouse = CreateWarehouseUseCaseTest.warehouse("MWH.001", "AMSTERDAM-001", 20, 10);
    store.create(warehouse);

    new ArchiveWarehouseUseCase(store).archive(warehouse);

    assertNotNull(warehouse.archivedAt);
    assertThrows(IllegalArgumentException.class, () -> new ArchiveWarehouseUseCase(store).archive(warehouse));
  }
}
