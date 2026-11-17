package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import java.time.LocalDateTime;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final LocationResolver locationResolver;

  public CreateWarehouseUseCase(WarehouseStore warehouseStore, LocationResolver locationResolver) {
    this.warehouseStore = warehouseStore;
    this.locationResolver = locationResolver;
  }

  @Override
  public void create(Warehouse warehouse) {
    validateRequiredFields(warehouse);
    if (warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode) != null) {
      throw new IllegalArgumentException("Business unit code already has an active warehouse.");
    }

    var location = locationResolver.resolveByIdentifier(warehouse.location);
    if (location == null) {
      throw new IllegalArgumentException("Warehouse location does not exist.");
    }

    var warehousesAtLocation = warehouseStore.getAll().stream()
        .filter(existing -> location.identification.equalsIgnoreCase(existing.location))
        .toList();
    if (warehousesAtLocation.size() >= location.maxNumberOfWarehouses) {
      throw new IllegalArgumentException("The location has reached its warehouse limit.");
    }
    int occupiedCapacity = warehousesAtLocation.stream().mapToInt(existing -> existing.capacity).sum();
    if ((long) occupiedCapacity + warehouse.capacity > location.maxCapacity) {
      throw new IllegalArgumentException("Warehouse capacity exceeds the location capacity.");
    }

    warehouse.location = location.identification;
    warehouse.createdAt = LocalDateTime.now();
    warehouse.archivedAt = null;
    warehouseStore.create(warehouse);
  }

  static void validateRequiredFields(Warehouse warehouse) {
    if (warehouse == null || warehouse.businessUnitCode == null || warehouse.businessUnitCode.isBlank()
        || warehouse.location == null || warehouse.location.isBlank()
        || warehouse.capacity == null || warehouse.stock == null) {
      throw new IllegalArgumentException("Business unit code, location, capacity and stock are required.");
    }
    if (warehouse.capacity < 0 || warehouse.stock < 0 || warehouse.stock > warehouse.capacity) {
      throw new IllegalArgumentException("Capacity and stock must be non-negative and stock cannot exceed capacity.");
    }
  }
}
