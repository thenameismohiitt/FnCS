package com.fulfilment.application.monolith.fulfillment.adapters.database;

import com.fulfilment.application.monolith.fulfillment.domain.model.FulfillmentAssignment;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Cacheable
@Table(name = "warehouse_fulfillment", uniqueConstraints =
    @UniqueConstraint(columnNames = {"storeId", "productId", "warehouseId"}))
public class DbFulfillmentAssignment {
  @Id @GeneratedValue public Long id;
  @Column(nullable = false) public Long storeId;
  @Column(nullable = false) public Long productId;
  @Column(nullable = false) public Long warehouseId;

  FulfillmentAssignment toDomain() {
    FulfillmentAssignment assignment = new FulfillmentAssignment();
    assignment.id = id;
    assignment.storeId = storeId;
    assignment.productId = productId;
    assignment.warehouseId = warehouseId;
    return assignment;
  }

  static DbFulfillmentAssignment from(FulfillmentAssignment assignment) {
    DbFulfillmentAssignment entity = new DbFulfillmentAssignment();
    entity.storeId = assignment.storeId;
    entity.productId = assignment.productId;
    entity.warehouseId = assignment.warehouseId;
    return entity;
  }
}
