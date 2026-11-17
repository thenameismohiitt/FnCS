package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.warehouse.api.WarehouseResource;
import com.warehouse.api.beans.Warehouse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import org.jboss.resteasy.reactive.ResponseStatus;

@RequestScoped
public class WarehouseResourceImpl implements WarehouseResource {

  @Inject private WarehouseRepository warehouseRepository;
  @Inject private CreateWarehouseOperation createWarehouse;
  @Inject private ArchiveWarehouseOperation archiveWarehouse;
  @Inject private ReplaceWarehouseOperation replaceWarehouse;

  @Override
  public List<Warehouse> listAllWarehousesUnits() {
    return warehouseRepository.getAll().stream().map(this::toWarehouseResponse).toList();
  }

  @Override
  @Transactional
  @ResponseStatus(201)
  public Warehouse createANewWarehouseUnit(@NotNull Warehouse data) {
    if (data.getId() != null) {
      throw new BadRequestException("Id must not be provided when creating a warehouse.");
    }
    var warehouse = toDomainWarehouse(data);
    try {
      createWarehouse.create(warehouse);
    } catch (IllegalArgumentException exception) {
      throw new BadRequestException(exception.getMessage(), exception);
    }
    return toWarehouseResponse(warehouse);
  }

  @Override
  public Warehouse getAWarehouseUnitByID(String id) {
    return toWarehouseResponse(requireActiveWarehouse(id));
  }

  @Override
  @Transactional
  public void archiveAWarehouseUnitByID(String id) {
    try {
      archiveWarehouse.archive(requireActiveWarehouse(id));
    } catch (IllegalArgumentException exception) {
      throw new BadRequestException(exception.getMessage(), exception);
    }
  }

  @Override
  @Transactional
  public Warehouse replaceTheCurrentActiveWarehouse(
      String businessUnitCode, @NotNull Warehouse data) {
    if (data.getId() != null) {
      throw new BadRequestException("Id must not be provided when replacing a warehouse.");
    }
    var current = warehouseRepository.findByBusinessUnitCode(businessUnitCode);
    if (current == null) {
      throw new NotFoundException("Active warehouse with business unit code " + businessUnitCode + " does not exist.");
    }
    if (data.getBusinessUnitCode() == null || !businessUnitCode.equalsIgnoreCase(data.getBusinessUnitCode())) {
      throw new BadRequestException("Replacement business unit code must match the path parameter.");
    }
    var warehouse = toDomainWarehouse(data);
    try {
      replaceWarehouse.replace(warehouse);
    } catch (IllegalArgumentException exception) {
      throw new BadRequestException(exception.getMessage(), exception);
    }
    return toWarehouseResponse(warehouse);
  }

  private Warehouse toWarehouseResponse(
      com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse) {
    var response = new Warehouse();
    response.setId(warehouse.id == null ? null : warehouse.id.toString());
    response.setBusinessUnitCode(warehouse.businessUnitCode);
    response.setLocation(warehouse.location);
    response.setCapacity(warehouse.capacity);
    response.setStock(warehouse.stock);

    return response;
  }

  private com.fulfilment.application.monolith.warehouses.domain.models.Warehouse toDomainWarehouse(
      Warehouse request) {
    var warehouse = new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();
    warehouse.businessUnitCode = request.getBusinessUnitCode();
    warehouse.location = request.getLocation();
    warehouse.capacity = request.getCapacity();
    warehouse.stock = request.getStock();
    return warehouse;
  }

  private com.fulfilment.application.monolith.warehouses.domain.models.Warehouse requireActiveWarehouse(String id) {
    final long warehouseId;
    try {
      warehouseId = Long.parseLong(id);
    } catch (NumberFormatException exception) {
      throw new NotFoundException("Warehouse with id " + id + " does not exist.");
    }
    var warehouse = warehouseRepository.findActiveById(warehouseId);
    if (warehouse == null) {
      throw new NotFoundException("Warehouse with id " + id + " does not exist.");
    }
    return warehouse;
  }
}
