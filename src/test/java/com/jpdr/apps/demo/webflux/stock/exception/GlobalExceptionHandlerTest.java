package com.jpdr.apps.demo.webflux.stock.exception;

import com.jpdr.apps.demo.webflux.stock.exception.dto.ErrorDto;
import com.jpdr.apps.demo.webflux.stock.exception.product.ProductNotFoundException;
import com.jpdr.apps.demo.webflux.stock.exception.product.ProductRepositoryException;
import com.jpdr.apps.demo.webflux.stock.exception.stock.InsufficientQuantityException;
import com.jpdr.apps.demo.webflux.stock.exception.stock.StockNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
  
  @InjectMocks
  GlobalExceptionHandler globalExceptionHandler;
  
  
  @Test
  @DisplayName("Error - MethodNotAllowedException")
  void givenMethodNotAllowedExceptionWhenHandleExceptionThenReturnError(){
    MethodNotAllowedException exception = new MethodNotAllowedException(HttpMethod.GET, List.of(HttpMethod.POST));
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
  
  @Test
  @DisplayName("Error - ServerWebInputException")
  void givenServerWebInputExceptionWhenHandleExceptionThenReturnError(){
    ServerWebInputException exception = new ServerWebInputException("");
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
  
  @Test
  @DisplayName("Error - ValidationException")
  void givenValidationExceptionWhenHandleValidationExceptionThenReturnError(){
    ValidationException exception = new ValidationException("");
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
  
  @Test
  @DisplayName("Error - ProductNotFoundException")
  void givenProductNotFoundExceptionWhenHandleExceptionThenReturnError(){
    ProductNotFoundException exception = new ProductNotFoundException(1, new RuntimeException());
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
  
  @Test
  @DisplayName("Error - StockNotFoundException")
  void givenStockNotFoundExceptionWhenHandleExceptionThenReturnError(){
    StockNotFoundException exception = new StockNotFoundException(1);
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
  
  
  @Test
  @DisplayName("Error - InsufficientQuantityException")
  void givenInsufficientQuantityExceptionWhenHandleExceptionThenReturnError(){
    InsufficientQuantityException exception = new InsufficientQuantityException(1,1);
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
  
  
  
  @Test
  @DisplayName("Error - RuntimeException")
  void givenProductRepositoryExceptionWhenHandleExceptionThenReturnError(){
    ProductRepositoryException exception = new ProductRepositoryException(1,new RuntimeException());
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
  
  @Test
  @DisplayName("Error - RuntimeException")
  void givenRuntimeExceptionWhenHandleRuntimeExceptionThenReturnError(){
    RuntimeException exception = new RuntimeException();
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
  
  @Test
  @DisplayName("Error - Exception")
  void givenExceptionWhenHandleExceptionThenReturnError(){
    Exception exception = new Exception();
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
  
}
