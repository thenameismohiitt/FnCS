package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {

  @Override
  public List<Warehouse> getAll() {
    return find("archivedAt is null").stream().map(DbWarehouse::toWarehouse).toList();
  }

  @Override
  @Transactional
  public void create(Warehouse warehouse) {
    DbWarehouse entity = new DbWarehouse();
    copy(warehouse, entity);
    persist(entity);
    warehouse.id = entity.id;
  }

  @Override
  @Transactional
  public void update(Warehouse warehouse) {
    if (warehouse.id == null) {
      throw new IllegalArgumentException("Warehouse id is required for an update.");
    }
    DbWarehouse entity = findById(warehouse.id);
    if (entity == null) {
      throw new IllegalArgumentException("Warehouse does not exist.");
    }
    copy(warehouse, entity);
  }

  @Override
  @Transactional
  public void remove(Warehouse warehouse) {
    if (warehouse.id != null) {
      deleteById(warehouse.id);
    }
  }

  @Override
  public Warehouse findByBusinessUnitCode(String buCode) {
    if (buCode == null) {
      return null;
    }
    DbWarehouse entity = find("lower(businessUnitCode) = ?1 and archivedAt is null", buCode.toLowerCase())
        .firstResult();
    return entity == null ? null : entity.toWarehouse();
  }

  @Override
  public Warehouse findActiveById(Long id) {
    if (id == null) {
      return null;
    }
    DbWarehouse entity = find("id = ?1 and archivedAt is null", id).firstResult();
    return entity == null ? null : entity.toWarehouse();
  }

  private void copy(Warehouse source, DbWarehouse target) {
    target.businessUnitCode = source.businessUnitCode;
    target.location = source.location;
    target.capacity = source.capacity;
    target.stock = source.stock;
    target.createdAt = source.createdAt;
    target.archivedAt = source.archivedAt;
  }
}
