package com.jpdr.apps.demo.webflux.stock.service;

import com.jpdr.apps.demo.webflux.stock.service.dto.stock.StockDto;
import com.jpdr.apps.demo.webflux.stock.service.dto.stock.StockTransactionDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AppService {
  
  Flux<StockDto> findAllStocks();
  Mono<StockDto> findStockByProductId(Integer productId);
  Mono<StockDto> createStock(StockDto stockDto);
  
  Flux<StockTransactionDto> findAllTransactions();
  Flux<StockTransactionDto> findTransactions(Integer productId);
  Flux<StockTransactionDto> findTransactionsByProductId(Integer productId);
  Mono<StockTransactionDto> createTransaction(Integer productId, StockTransactionDto transactionDto);
  
}
