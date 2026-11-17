package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import java.util.ArrayList;
import java.util.List;

final class InMemoryWarehouseStore implements WarehouseStore {

  private final List<Warehouse> warehouses = new ArrayList<>();
  private long nextId = 1;

  @Override
  public List<Warehouse> getAll() {
    return warehouses.stream().filter(warehouse -> warehouse.archivedAt == null).toList();
  }

  @Override
  public void create(Warehouse warehouse) {
    warehouse.id = nextId++;
    warehouses.add(warehouse);
  }

  @Override
  public void update(Warehouse warehouse) {
    // Warehouses are stored by reference, matching JPA's managed-entity behaviour.
  }

  @Override
  public void remove(Warehouse warehouse) {
    warehouses.remove(warehouse);
  }

  @Override
  public Warehouse findByBusinessUnitCode(String businessUnitCode) {
    return getAll().stream()
        .filter(warehouse -> warehouse.businessUnitCode.equalsIgnoreCase(businessUnitCode))
        .findFirst()
        .orElse(null);
  }

  @Override
  public Warehouse findActiveById(Long id) {
    return getAll().stream().filter(warehouse -> warehouse.id.equals(id)).findFirst().orElse(null);
  }
}
