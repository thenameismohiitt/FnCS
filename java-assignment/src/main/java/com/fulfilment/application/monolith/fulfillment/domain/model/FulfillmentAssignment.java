package com.fulfilment.application.monolith.fulfillment.domain.model;

/** A warehouse assigned to fulfil one product for one store. */
public class FulfillmentAssignment {
  public Long id;
  public Long storeId;
  public Long productId;
  public Long warehouseId;
}
