package com.renansouza.so.os;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class OSNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(OSNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String osNotFoundHandler(OSNotFoundException ex) {
    return ex.getMessage();
  }

}