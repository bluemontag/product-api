package com.textplus.productapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

  private Logger logger = Logger.getLogger(this.getClass().getName());

  /**
   * Main exception handler
   *
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(Exception.class)
  public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
    List<String> details = new ArrayList<>();
    details.add(ex.getLocalizedMessage());
    return new ResponseEntity<>(details, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * This method handles validation errors in the controllers and the tests.
   * 
   * @param ex
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({org.springframework.web.bind.MethodArgumentNotValidException.class,
                     javax.validation.ConstraintViolationException.class})
  public void validationErrors(Exception ex) {
    logger.log(Level.WARNING, "Status code 400 (Bad Request) returned. Validation Errors:\n");
    logger.log(Level.WARNING, ex.getLocalizedMessage());
  }

}
