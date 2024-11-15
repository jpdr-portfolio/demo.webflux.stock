package com.jpdr.apps.demo.webflux.stock.service;

import com.jpdr.apps.demo.webflux.stock.service.dto.stock.StockDto;
import com.jpdr.apps.demo.webflux.stock.service.dto.stock.StockTransactionDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AppService {
  
  Mono<List<StockDto>> findAllStocks();
  Mono<StockDto> findStockByProductId(Integer productId);
  Mono<StockDto> createStock(StockDto stockDto);
  
  Mono<List<StockTransactionDto>> findAllTransactions();
  Mono<List<StockTransactionDto>> findTransactions(Integer productId);
  Mono<List<StockTransactionDto>> findTransactionsByProductId(Integer productId);
  Mono<StockTransactionDto> createTransaction(Integer productId, StockTransactionDto transactionDto);
  
}
