package com.jpdr.apps.demo.webflux.stock.repository.stock;

import com.jpdr.apps.demo.webflux.stock.model.Stock;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends ReactiveCrudRepository<Stock, Long> {

}
