package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import java.time.LocalDateTime;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final LocationResolver locationResolver;

  public ReplaceWarehouseUseCase(WarehouseStore warehouseStore, LocationResolver locationResolver) {
    this.warehouseStore = warehouseStore;
    this.locationResolver = locationResolver;
  }

  @Override
  public void replace(Warehouse newWarehouse) {
    CreateWarehouseUseCase.validateRequiredFields(newWarehouse);
    Warehouse previous = warehouseStore.findByBusinessUnitCode(newWarehouse.businessUnitCode);
    if (previous == null) {
      throw new IllegalArgumentException("Active warehouse does not exist.");
    }
    if (!newWarehouse.stock.equals(previous.stock)) {
      throw new IllegalArgumentException("Replacement warehouse stock must match the current warehouse stock.");
    }
    if (newWarehouse.capacity < previous.stock) {
      throw new IllegalArgumentException("Replacement warehouse capacity cannot accommodate the current stock.");
    }
    var location = locationResolver.resolveByIdentifier(newWarehouse.location);
    if (location == null) {
      throw new IllegalArgumentException("Warehouse location does not exist.");
    }
    var otherWarehousesAtLocation = warehouseStore.getAll().stream()
        .filter(existing -> existing.id != null && !existing.id.equals(previous.id))
        .filter(existing -> location.identification.equalsIgnoreCase(existing.location))
        .toList();
    if (otherWarehousesAtLocation.size() >= location.maxNumberOfWarehouses) {
      throw new IllegalArgumentException("The location has reached its warehouse limit.");
    }
    int occupiedCapacity = otherWarehousesAtLocation.stream().mapToInt(existing -> existing.capacity).sum();
    if ((long) occupiedCapacity + newWarehouse.capacity > location.maxCapacity) {
      throw new IllegalArgumentException("Warehouse capacity exceeds the location capacity.");
    }

    previous.archivedAt = LocalDateTime.now();
    warehouseStore.update(previous);
    newWarehouse.location = location.identification;
    newWarehouse.createdAt = LocalDateTime.now();
    newWarehouse.archivedAt = null;
    warehouseStore.create(newWarehouse);
  }
}
