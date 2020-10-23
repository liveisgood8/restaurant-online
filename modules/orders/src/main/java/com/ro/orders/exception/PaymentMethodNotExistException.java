package com.ro.orders.exception;

public class PaymentMethodNotExistException extends RuntimeException {
  public PaymentMethodNotExistException(String methodName) {
    super("Payment method '" + methodName + "' is not exist");
  }
}
