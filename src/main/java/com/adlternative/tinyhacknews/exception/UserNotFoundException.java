package com.adlternative.tinyhacknews.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends WebException {
  public UserNotFoundException(String message) {
    super(HttpStatus.NOT_FOUND, message);
  }
}
