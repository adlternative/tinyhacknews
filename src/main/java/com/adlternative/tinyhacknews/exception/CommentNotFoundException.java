package com.adlternative.tinyhacknews.exception;

import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends WebException {
  public CommentNotFoundException(String message) {
    super(HttpStatus.NOT_FOUND, message);
  }
}
