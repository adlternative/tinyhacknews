package com.adlternative.tinyhacknews.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalErrorException extends RuntimeException {
  public InternalErrorException(String message) {
    super(message);
  }

  public InternalErrorException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
