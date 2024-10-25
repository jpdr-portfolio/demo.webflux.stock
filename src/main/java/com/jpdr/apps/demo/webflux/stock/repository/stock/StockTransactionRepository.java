package com.jpdr.apps.demo.webflux.stock.repository.stock;

import com.jpdr.apps.demo.webflux.stock.model.StockTransaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface StockTransactionRepository extends ReactiveCrudRepository<StockTransaction, Integer> {
  
  Flux<StockTransaction> findAllByProductId(Integer productId);
  
}
