package com.fulfilment.application.monolith.fulfillment.application.validator;

public class InvalidFulfillmentException extends IllegalArgumentException {
  public InvalidFulfillmentException(String message) { super(message); }
}
