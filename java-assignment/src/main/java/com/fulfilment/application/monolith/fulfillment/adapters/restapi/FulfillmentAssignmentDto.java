package com.fulfilment.application.monolith.fulfillment.adapters.restapi;

import com.fulfilment.application.monolith.fulfillment.domain.model.FulfillmentAssignment;

public class FulfillmentAssignmentDto {
  public Long id;
  public Long storeId;
  public Long productId;
  public Long warehouseId;

  static FulfillmentAssignment toDomain(FulfillmentAssignmentDto dto) {
    if (dto == null) return null;
    FulfillmentAssignment assignment = new FulfillmentAssignment();
    assignment.id = dto.id;
    assignment.storeId = dto.storeId;
    assignment.productId = dto.productId;
    assignment.warehouseId = dto.warehouseId;
    return assignment;
  }

  static FulfillmentAssignmentDto from(FulfillmentAssignment assignment) {
    FulfillmentAssignmentDto dto = new FulfillmentAssignmentDto();
    dto.id = assignment.id;
    dto.storeId = assignment.storeId;
    dto.productId = assignment.productId;
    dto.warehouseId = assignment.warehouseId;
    return dto;
  }
}
