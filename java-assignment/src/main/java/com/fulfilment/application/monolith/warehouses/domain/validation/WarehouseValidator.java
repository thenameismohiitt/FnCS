package com.fulfilment.application.monolith.warehouses.domain.validation;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import java.util.List;

public class WarehouseValidator {

  private final WarehouseStore warehouseStore;
  private final LocationResolver locationResolver;

  public WarehouseValidator(WarehouseStore warehouseStore, LocationResolver locationResolver) {
    this.warehouseStore = warehouseStore;
    this.locationResolver = locationResolver;
  }

  public Location validateForCreation(Warehouse warehouse) {
    validateRequiredFields(warehouse);
    if (warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode) != null) {
      throw new IllegalArgumentException("Business unit code already has an active warehouse.");
    }
    Location location = resolveLocation(warehouse.location);
    validateLocationCapacity(location, warehouse, null);
    return location;
  }

  public Replacement validateForReplacement(Warehouse warehouse) {
    validateRequiredFields(warehouse);
    Warehouse previous = warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode);
    if (previous == null) {
      throw new IllegalArgumentException("Active warehouse does not exist.");
    }
    if (!warehouse.stock.equals(previous.stock)) {
      throw new IllegalArgumentException("Replacement warehouse stock must match the current warehouse stock.");
    }
    if (warehouse.capacity < previous.stock) {
      throw new IllegalArgumentException(
          "Replacement warehouse capacity cannot accommodate the current stock.");
    }
    Location location = resolveLocation(warehouse.location);
    validateLocationCapacity(location, warehouse, previous.id);
    return new Replacement(previous, location);
  }

  private Location resolveLocation(String identifier) {
    Location location = locationResolver.resolveByIdentifier(identifier);
    if (location == null) {
      throw new IllegalArgumentException("Warehouse location does not exist.");
    }
    return location;
  }

  private void validateLocationCapacity(Location location, Warehouse warehouse, Long excludedWarehouseId) {
    List<Warehouse> warehousesAtLocation = warehouseStore.getAll().stream()
        .filter(existing -> excludedWarehouseId == null || !excludedWarehouseId.equals(existing.id))
        .filter(existing -> location.identification.equalsIgnoreCase(existing.location))
        .toList();
    if (warehousesAtLocation.size() >= location.maxNumberOfWarehouses) {
      throw new IllegalArgumentException("The location has reached its warehouse limit.");
    }
    int occupiedCapacity = warehousesAtLocation.stream().mapToInt(existing -> existing.capacity).sum();
    if ((long) occupiedCapacity + warehouse.capacity > location.maxCapacity) {
      throw new IllegalArgumentException("Warehouse capacity exceeds the location capacity.");
    }
  }

  private void validateRequiredFields(Warehouse warehouse) {
    if (warehouse == null || warehouse.businessUnitCode == null || warehouse.businessUnitCode.isBlank()
        || warehouse.location == null || warehouse.location.isBlank()
        || warehouse.capacity == null || warehouse.stock == null) {
      throw new IllegalArgumentException("Business unit code, location, capacity and stock are required.");
    }
    if (warehouse.capacity < 0 || warehouse.stock < 0 || warehouse.stock > warehouse.capacity) {
      throw new IllegalArgumentException(
          "Capacity and stock must be non-negative and stock cannot exceed capacity.");
    }
  }

  public record Replacement(Warehouse previous, Location location) {}
}
