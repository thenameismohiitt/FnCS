package com.fulfilment.application.monolith.fulfillment.application.ports;

/** Port for verifying aggregates referenced by a fulfilment assignment. */
public interface FulfillmentReferenceCatalog {
  boolean storeExists(Long id);
  boolean productExists(Long id);
  boolean activeWarehouseExists(Long id);
}
