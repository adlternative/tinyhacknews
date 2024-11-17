package com.adlternative.tinyhacknews.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class WebException extends RuntimeException {
  private final HttpStatus code;
  private final String message;

  public WebException(HttpStatus code, String message) {
    super(message);
    this.code = code;
    this.message = message;
  }

  public WebException(HttpStatus code, String message, Throwable throwable) {
    super(message, throwable);
    this.code = code;
    this.message = message;
  }
}
