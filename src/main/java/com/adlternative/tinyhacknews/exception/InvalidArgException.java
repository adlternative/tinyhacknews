package com.adlternative.tinyhacknews.exception;

import org.springframework.http.HttpStatus;

public class InvalidArgException extends WebException {
  public InvalidArgException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }
}
