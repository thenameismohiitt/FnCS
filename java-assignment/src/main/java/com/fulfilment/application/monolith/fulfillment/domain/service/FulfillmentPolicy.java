package com.fulfilment.application.monolith.fulfillment.domain.service;

import com.fulfilment.application.monolith.fulfillment.domain.model.FulfillmentAssignment;
import java.util.List;

/** Domain constraints for warehouse fulfilment assignments. */
public final class FulfillmentPolicy {
  public static final int MAX_WAREHOUSES_PER_PRODUCT_AND_STORE = 2;
  public static final int MAX_WAREHOUSES_PER_STORE = 3;
  public static final int MAX_PRODUCT_TYPES_PER_WAREHOUSE = 5;

  private FulfillmentPolicy() {}

  public static void validateNewAssignment(
      List<? extends FulfillmentAssignment> assignments, FulfillmentAssignment candidate) {
    boolean duplicate = assignments.stream().anyMatch(existing -> same(existing, candidate));
    if (duplicate) {
      throw new IllegalArgumentException("This warehouse is already assigned to the product at this store.");
    }
    long warehousesForProductAtStore = assignments.stream()
        .filter(existing -> existing.storeId.equals(candidate.storeId)
            && existing.productId.equals(candidate.productId))
        .map(existing -> existing.warehouseId).distinct().count();
    if (warehousesForProductAtStore >= MAX_WAREHOUSES_PER_PRODUCT_AND_STORE) {
      throw new IllegalArgumentException("A product can be fulfilled by at most two warehouses per store.");
    }
    boolean warehouseAlreadyServesStore = assignments.stream().anyMatch(existing ->
        existing.storeId.equals(candidate.storeId) && existing.warehouseId.equals(candidate.warehouseId));
    long warehousesForStore = assignments.stream().filter(existing ->
        existing.storeId.equals(candidate.storeId)).map(existing -> existing.warehouseId).distinct().count();
    if (!warehouseAlreadyServesStore && warehousesForStore >= MAX_WAREHOUSES_PER_STORE) {
      throw new IllegalArgumentException("A store can be fulfilled by at most three warehouses.");
    }
    boolean warehouseAlreadyHasProduct = assignments.stream().anyMatch(existing ->
        existing.warehouseId.equals(candidate.warehouseId) && existing.productId.equals(candidate.productId));
    long productsInWarehouse = assignments.stream().filter(existing ->
        existing.warehouseId.equals(candidate.warehouseId)).map(existing -> existing.productId).distinct().count();
    if (!warehouseAlreadyHasProduct && productsInWarehouse >= MAX_PRODUCT_TYPES_PER_WAREHOUSE) {
      throw new IllegalArgumentException("A warehouse can fulfil at most five product types.");
    }
  }

  private static boolean same(FulfillmentAssignment left, FulfillmentAssignment right) {
    return left.storeId.equals(right.storeId) && left.productId.equals(right.productId)
        && left.warehouseId.equals(right.warehouseId);
  }
}
