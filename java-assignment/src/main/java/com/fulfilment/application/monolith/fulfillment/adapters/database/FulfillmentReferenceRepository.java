package com.fulfilment.application.monolith.fulfillment.adapters.database;

import com.fulfilment.application.monolith.fulfillment.application.ports.FulfillmentReferenceCatalog;
import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FulfillmentReferenceRepository implements FulfillmentReferenceCatalog {
  @Inject ProductRepository productRepository;
  @Inject WarehouseRepository warehouseRepository;

  @Override public boolean storeExists(Long id) { return id != null && Store.findById(id) != null; }
  @Override public boolean productExists(Long id) { return id != null && productRepository.findById(id) != null; }
  @Override public boolean activeWarehouseExists(Long id) {
    return id != null && warehouseRepository.findActiveById(id) != null;
  }
}
