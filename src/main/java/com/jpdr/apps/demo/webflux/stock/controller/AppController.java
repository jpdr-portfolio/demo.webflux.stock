package com.jpdr.apps.demo.webflux.stock.controller;

import com.jpdr.apps.demo.webflux.stock.service.AppService;
import com.jpdr.apps.demo.webflux.stock.service.dto.stock.StockDto;
import com.jpdr.apps.demo.webflux.stock.service.dto.stock.StockTransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AppController {
  
  private final AppService appService;
  
  @GetMapping("/stock")
  public ResponseEntity<Flux<StockDto>> getAllStock(){
    return new ResponseEntity<>(appService.findAllStocks(), HttpStatus.OK);
  }
  
  @GetMapping("/stock/{productId}")
  public ResponseEntity<Mono<StockDto>> getStockByProductId(
    @PathVariable(name = "productId") Integer productId){
    return new ResponseEntity<>(appService.findStockByProductId(productId), HttpStatus.OK);
  }
  
  @PostMapping("/stock")
  public ResponseEntity<Mono<StockDto>> createStock(
    @RequestBody StockDto stockDto){
    return new ResponseEntity<>(appService.createStock(stockDto), HttpStatus.CREATED);
  }
  
  @GetMapping("/stock/{productId}/transactions")
  public ResponseEntity<Flux<StockTransactionDto>> getTransactions(
    @PathVariable(name = "productId", required = false) Integer productId){
    return new ResponseEntity<>(appService.findTransactions(productId), HttpStatus.OK);
  }
  
  @PostMapping("/stock/{productId}/transactions")
  public ResponseEntity<Mono<StockTransactionDto>> createTransaction(
    @PathVariable(name = "productId") Integer productId,
    @RequestBody StockTransactionDto transactionDto){
    return new ResponseEntity<>(appService.createTransaction(productId, transactionDto), HttpStatus.CREATED);
  }
  
}
