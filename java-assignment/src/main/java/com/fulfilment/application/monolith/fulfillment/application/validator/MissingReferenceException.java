package com.fulfilment.application.monolith.fulfillment.application.validator;

public class MissingReferenceException extends RuntimeException {
  public MissingReferenceException(String referenceName, Long id) {
    super(referenceName + " with id " + id + " does not exist.");
  }
}
