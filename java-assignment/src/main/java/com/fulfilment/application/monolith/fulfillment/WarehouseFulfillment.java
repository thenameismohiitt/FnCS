package com.fulfilment.application.monolith.fulfillment;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Cacheable
@Table(
    name = "warehouse_fulfillment",
    uniqueConstraints = @UniqueConstraint(columnNames = {"storeId", "productId", "warehouseId"}))
public class WarehouseFulfillment {

  @Id @GeneratedValue public Long id;

  @Column(nullable = false)
  public Long storeId;

  @Column(nullable = false)
  public Long productId;

  @Column(nullable = false)
  public Long warehouseId;
}
