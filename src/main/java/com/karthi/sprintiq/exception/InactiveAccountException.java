package com.karthi.sprintiq.exception;

public class InactiveAccountException extends RuntimeException {

  public InactiveAccountException(String email) {
    super("Inactive account: " + email);
  }
}
