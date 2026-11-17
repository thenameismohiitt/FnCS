package com.fulfilment.application.monolith.fulfillment;

import java.util.List;

/** Business constraints for assigning warehouses to fulfil products for stores. */
final class FulfillmentRules {

  static final int MAX_WAREHOUSES_PER_PRODUCT_AND_STORE = 2;
  static final int MAX_WAREHOUSES_PER_STORE = 3;
  static final int MAX_PRODUCT_TYPES_PER_WAREHOUSE = 5;

  private FulfillmentRules() {}

  static void validateNewAssignment(
      List<WarehouseFulfillment> assignments, WarehouseFulfillment candidate) {
    boolean duplicate = assignments.stream().anyMatch(existing ->
        existing.storeId.equals(candidate.storeId)
            && existing.productId.equals(candidate.productId)
            && existing.warehouseId.equals(candidate.warehouseId));
    if (duplicate) {
      throw new IllegalArgumentException(
          "This warehouse is already assigned to the product at this store.");
    }

    long warehousesForProductAtStore = assignments.stream()
        .filter(existing -> existing.storeId.equals(candidate.storeId)
            && existing.productId.equals(candidate.productId))
        .map(existing -> existing.warehouseId)
        .distinct()
        .count();
    if (warehousesForProductAtStore >= MAX_WAREHOUSES_PER_PRODUCT_AND_STORE) {
      throw new IllegalArgumentException(
          "A product can be fulfilled by at most two warehouses per store.");
    }

    boolean warehouseAlreadyServesStore = assignments.stream().anyMatch(existing ->
        existing.storeId.equals(candidate.storeId)
            && existing.warehouseId.equals(candidate.warehouseId));
    long warehousesForStore = assignments.stream()
        .filter(existing -> existing.storeId.equals(candidate.storeId))
        .map(existing -> existing.warehouseId)
        .distinct()
        .count();
    if (!warehouseAlreadyServesStore && warehousesForStore >= MAX_WAREHOUSES_PER_STORE) {
      throw new IllegalArgumentException("A store can be fulfilled by at most three warehouses.");
    }

    boolean warehouseAlreadyHasProduct = assignments.stream().anyMatch(existing ->
        existing.warehouseId.equals(candidate.warehouseId)
            && existing.productId.equals(candidate.productId));
    long productsInWarehouse = assignments.stream()
        .filter(existing -> existing.warehouseId.equals(candidate.warehouseId))
        .map(existing -> existing.productId)
        .distinct()
        .count();
    if (!warehouseAlreadyHasProduct && productsInWarehouse >= MAX_PRODUCT_TYPES_PER_WAREHOUSE) {
      throw new IllegalArgumentException("A warehouse can fulfil at most five product types.");
    }
  }
}
