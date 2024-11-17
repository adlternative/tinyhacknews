package com.adlternative.tinyhacknews.exception;

import org.springframework.http.HttpStatus;

public class DBException extends WebException {
  public DBException(String message) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, message);
  }
}
