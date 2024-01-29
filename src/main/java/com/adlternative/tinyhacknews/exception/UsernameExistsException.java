package com.adlternative.tinyhacknews.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UsernameExistsException extends RuntimeException {
  public UsernameExistsException(String message) {
    super(message);
  }
}
