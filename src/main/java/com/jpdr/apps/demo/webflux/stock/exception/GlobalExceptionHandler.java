package com.jpdr.apps.demo.webflux.stock.exception;

import com.jpdr.apps.demo.webflux.stock.exception.dto.ErrorDto;
import com.jpdr.apps.demo.webflux.stock.exception.product.ProductNotFoundException;
import com.jpdr.apps.demo.webflux.stock.exception.product.ProductRepositoryException;
import com.jpdr.apps.demo.webflux.stock.exception.stock.InsufficientQuantityException;
import com.jpdr.apps.demo.webflux.stock.exception.stock.StockNotFoundException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  
  @ExceptionHandler(MethodNotAllowedException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(MethodNotAllowedException ex){
    log.warn(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto(ex.getMessage());
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(ServerWebInputException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(ServerWebInputException ex){
    log.warn(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto(ex.getMessage());
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(ValidationException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(ValidationException ex){
    log.warn(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto(ex.getMessage());
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.BAD_REQUEST);
  }
  
  
  @ExceptionHandler(ProductNotFoundException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(ProductNotFoundException ex){
    log.warn(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto(ex.getMessage());
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.BAD_REQUEST);
  }
  
  
  @ExceptionHandler(StockNotFoundException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(StockNotFoundException ex){
    log.warn(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto(ex.getMessage());
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.NOT_FOUND);
  }
  
  @ExceptionHandler(InsufficientQuantityException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(InsufficientQuantityException ex){
    log.warn(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto(ex.getMessage());
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(ProductRepositoryException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(ProductRepositoryException ex){
    log.error(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto("An error has occurred");
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  
  @ExceptionHandler(RuntimeException.class)
  ResponseEntity<Mono<ErrorDto>> handleException(RuntimeException ex){
    log.error(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto("An error has occurred");
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  @ExceptionHandler(Exception.class)
  ResponseEntity<Mono<ErrorDto>> handleException(Exception ex){
    log.error(ExceptionUtils.getStackTrace(ex));
    ErrorDto errorDto = new ErrorDto("An error has occurred");
    return new ResponseEntity<>(Mono.just(errorDto), HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
