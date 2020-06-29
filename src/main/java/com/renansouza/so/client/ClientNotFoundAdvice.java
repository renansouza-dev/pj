package com.renansouza.so.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class ClientNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(ClientNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String clientNotFoundHandler(ClientNotFoundException ex) {
    return ex.getMessage();
  }

}