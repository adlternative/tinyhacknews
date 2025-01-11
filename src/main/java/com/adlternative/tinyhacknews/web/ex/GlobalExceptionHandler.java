package com.adlternative.tinyhacknews.web.ex;

import com.adlternative.tinyhacknews.exception.WebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseBody
  public ResponseEntity<String> handleConversionException(MethodArgumentTypeMismatchException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid value for parameter");
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<Object> handlerException(Exception ex) {
    log.info("handlerException", ex);
    if (ex instanceof WebException) {
      WebException webException = (WebException) ex;
      return ResponseEntity.status(webException.getCode().value()).body(webException.getMessage());
    } else {
      return ResponseEntity.status(500).body(ex.getMessage());
    }
  }
}
