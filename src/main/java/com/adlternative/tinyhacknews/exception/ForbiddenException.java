package com.adlternative.tinyhacknews.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends WebException {
  public ForbiddenException(String message) {
    super(HttpStatus.FORBIDDEN, message);
  }
}
