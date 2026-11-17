package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fulfilment.application.monolith.location.LocationGateway;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class CreateWarehouseUseCaseTest {

  @Test
  void createsWarehouseAtAValidLocation() {
    var store = new InMemoryWarehouseStore();
    var useCase = new CreateWarehouseUseCase(store, new LocationGateway());
    var warehouse = warehouse("MWH.NEW", "AMSTERDAM-001", 40, 20);

    useCase.create(warehouse);

    assertNotNull(warehouse.id);
    assertNotNull(warehouse.createdAt);
    assertEquals("AMSTERDAM-001", warehouse.location);
    assertEquals(1, store.getAll().size());
  }

  @Test
  void rejectsDuplicateActiveBusinessUnitCode() {
    var store = new InMemoryWarehouseStore();
    store.create(warehouse("MWH.001", "AMSTERDAM-001", 20, 1));
    var useCase = new CreateWarehouseUseCase(store, new LocationGateway());

    assertThrows(IllegalArgumentException.class,
        () -> useCase.create(warehouse("mwh.001", "AMSTERDAM-001", 20, 1)));
  }

  @Test
  void rejectsUnknownLocationsAndInvalidStock() {
    var useCase = new CreateWarehouseUseCase(new InMemoryWarehouseStore(), new LocationGateway());

    assertThrows(IllegalArgumentException.class,
        () -> useCase.create(warehouse("MWH.002", "UNKNOWN", 20, 1)));
    assertThrows(IllegalArgumentException.class,
        () -> useCase.create(warehouse("MWH.003", "AMSTERDAM-001", 5, 6)));
  }

  @Test
  void rejectsLocationWarehouseAndCapacityLimits() {
    var locationGateway = new LocationGateway();
    var countStore = new InMemoryWarehouseStore();
    countStore.create(warehouse("MWH.001", "ZWOLLE-001", 20, 1));
    var countUseCase = new CreateWarehouseUseCase(countStore, locationGateway);
    assertThrows(IllegalArgumentException.class,
        () -> countUseCase.create(warehouse("MWH.002", "ZWOLLE-001", 10, 1)));

    var capacityStore = new InMemoryWarehouseStore();
    capacityStore.create(warehouse("MWH.003", "AMSTERDAM-001", 80, 1));
    var capacityUseCase = new CreateWarehouseUseCase(capacityStore, locationGateway);
    assertThrows(IllegalArgumentException.class,
        () -> capacityUseCase.create(warehouse("MWH.004", "AMSTERDAM-001", 30, 1)));
  }

  static Warehouse warehouse(String businessUnitCode, String location, int capacity, int stock) {
    var warehouse = new Warehouse();
    warehouse.businessUnitCode = businessUnitCode;
    warehouse.location = location;
    warehouse.capacity = capacity;
    warehouse.stock = stock;
    return warehouse;
  }
}
