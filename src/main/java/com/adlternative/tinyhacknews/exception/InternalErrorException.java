package com.adlternative.tinyhacknews.exception;

import org.springframework.http.HttpStatus;

public class InternalErrorException extends WebException {
  public InternalErrorException(String message) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, message);
  }

  public InternalErrorException(String message, Throwable throwable) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, message, throwable);
  }
}
