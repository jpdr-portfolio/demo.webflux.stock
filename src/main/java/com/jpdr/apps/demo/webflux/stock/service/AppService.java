package com.jpdr.apps.demo.webflux.stock.service;

import com.jpdr.apps.demo.webflux.stock.service.dto.stock.StockDto;
import com.jpdr.apps.demo.webflux.stock.service.dto.stock.StockTransactionDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AppService {
  
  Mono<List<StockDto>> findAllStocks();
  Mono<StockDto> findStockByProductId(Long productId);
  Mono<StockDto> createStock(StockDto stockDto);
  
  Mono<List<StockTransactionDto>> findAllTransactions();
  Mono<List<StockTransactionDto>> findTransactions(Long productId);
  Mono<List<StockTransactionDto>> findTransactionsByProductId(Long productId);
  Mono<StockTransactionDto> createTransaction(Long productId, StockTransactionDto transactionDto);
  
}
