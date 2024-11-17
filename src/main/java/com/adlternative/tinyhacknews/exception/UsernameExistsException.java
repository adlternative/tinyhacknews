package com.adlternative.tinyhacknews.exception;

import org.springframework.http.HttpStatus;

public class UsernameExistsException extends WebException {
  public UsernameExistsException(String message) {
    super(HttpStatus.CONFLICT, message);
  }
}
