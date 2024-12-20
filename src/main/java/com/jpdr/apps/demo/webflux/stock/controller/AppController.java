package com.jpdr.apps.demo.webflux.stock.controller;

import com.jpdr.apps.demo.webflux.eventlogger.component.EventLogger;
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
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AppController {
  
  private final AppService appService;
  private final EventLogger eventLogger;
  
  @GetMapping("/stock")
  public Mono<ResponseEntity<List<StockDto>>> getAllStock(){
    return this.appService.findAllStocks()
      .doOnNext(stock -> this.eventLogger.logEvent("getAllStock", stock))
      .map(stock -> new ResponseEntity<>(stock, HttpStatus.OK));
  }
  
  @GetMapping("/stock/{productId}")
  public Mono<ResponseEntity<StockDto>> getStockByProductId(
    @PathVariable(name = "productId") Integer productId){
    return this.appService.findStockByProductId(productId)
      .doOnNext(stock -> this.eventLogger.logEvent("findStockByProductId", stock))
      .map(stock -> new ResponseEntity<>(stock, HttpStatus.OK));
  }
  
  @PostMapping("/stock")
  public Mono<ResponseEntity<StockDto>> createStock(@RequestBody StockDto stockDto){
    return this.appService.createStock(stockDto)
      .doOnNext(stock -> this.eventLogger.logEvent("createStock", stock))
      .map(stock -> new ResponseEntity<>(stock, HttpStatus.CREATED));
  }
  
  @GetMapping("/stock/{productId}/transactions")
  public Mono<ResponseEntity<List<StockTransactionDto>>> getTransactions(
    @PathVariable(name = "productId", required = false) Integer productId){
    return this.appService.findTransactions(productId)
      .doOnNext(transactions ->
        this.eventLogger.logEvent("getTransactions", transactions))
      .map(transactions -> new ResponseEntity<>(transactions, HttpStatus.OK));
  }
  
  @PostMapping("/stock/{productId}/transactions")
  public Mono<ResponseEntity<StockTransactionDto>> createTransaction(
    @PathVariable(name = "productId") Integer productId,
    @RequestBody StockTransactionDto transactionDto){
    return this.appService.createTransaction(productId, transactionDto)
      .doOnNext(transaction ->
        this.eventLogger.logEvent("getTransactions", transaction))
      .map(transaction -> new ResponseEntity<>(transaction, HttpStatus.CREATED));
  }
  
}
