package com.adlternative.tinyhacknews.exception;

import org.springframework.http.HttpStatus;

public class NewsNotFoundException extends WebException {
  public NewsNotFoundException(String message) {
    super(HttpStatus.NOT_FOUND, message);
  }
}
