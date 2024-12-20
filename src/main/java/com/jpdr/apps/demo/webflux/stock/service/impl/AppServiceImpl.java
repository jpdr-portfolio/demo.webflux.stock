package com.jpdr.apps.demo.webflux.stock.service.impl;

import com.jpdr.apps.demo.webflux.stock.exception.stock.StockNotFoundException;
import com.jpdr.apps.demo.webflux.stock.helper.StockTransactionHelper;
import com.jpdr.apps.demo.webflux.stock.model.Stock;
import com.jpdr.apps.demo.webflux.stock.model.StockTransaction;
import com.jpdr.apps.demo.webflux.stock.repository.product.ProductRepository;
import com.jpdr.apps.demo.webflux.stock.repository.stock.StockRepository;
import com.jpdr.apps.demo.webflux.stock.repository.stock.StockTransactionRepository;
import com.jpdr.apps.demo.webflux.stock.service.AppService;
import com.jpdr.apps.demo.webflux.stock.service.dto.stock.StockDto;
import com.jpdr.apps.demo.webflux.stock.service.dto.stock.StockTransactionDto;
import com.jpdr.apps.demo.webflux.stock.service.mapper.StockMapper;
import com.jpdr.apps.demo.webflux.stock.service.mapper.StockTransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {
  
  private final StockRepository stockRepository;
  private final StockTransactionRepository transactionRepository;
  private final ProductRepository productRepository;
  
  
  @Override
  public Mono<List<StockDto>> findAllStocks() {
    log.debug("findAllStocks");
    return this.stockRepository.findAll()
      .flatMap(stock ->
        Flux.zip(
          Mono.just(stock).repeat(),
          Flux.from(this.productRepository.getProductById(stock.getProductId()))))
      .map(tuple -> {
        StockDto stockDto = StockMapper.INSTANCE.entityToDto(tuple.getT1());
        stockDto.setProductName(tuple.getT2().getName());
        return stockDto;
      })
      .doOnNext(stockDto -> log.debug(stockDto.toString()))
      .collectList();
  }
  
  @Override
  @Cacheable(key = "#productId", value = "stock", sync = true)
  public Mono<StockDto> findStockByProductId(Integer productId) {
    log.debug("findStockByProductId");
    return this.productRepository.getProductById(productId)
      .flatMap(productDto ->
        Mono.zip(
          Mono.just(productDto),
          Mono.from(this.stockRepository.findById(productDto.getId())
            .switchIfEmpty(Mono.error(new StockNotFoundException(productDto.getId()))))))
      .map(tuple -> {
          StockDto stockDto = StockMapper.INSTANCE.entityToDto(tuple.getT2());
          stockDto.setProductName(tuple.getT1().getName());
          return stockDto;
        })
      .doOnNext(stockDto -> log.debug(stockDto.toString()));
  }
  
  @Override
  @Transactional
  public Mono<StockDto> createStock(StockDto stockDto) {
    log.debug("createStock");
    return Mono.zip(
        Mono.just(stockDto),
        Mono.from(this.productRepository.getProductById(stockDto.getProductId())))
      .map(tuple -> {
        Stock stock = StockMapper.INSTANCE.dtoToEntity(tuple.getT1());
        stock.setLastTransactionId(null);
        stock.setLastTransactionDate(null);
        stock.setNewEntity(true);
        return Tuples.of(stock, tuple.getT2());
      })
      .flatMap(tuple ->
        Mono.zip(
          Mono.from(this.stockRepository.save(tuple.getT1())),
          Mono.just(tuple.getT2())))
      .map(tuple -> {
        StockDto createdStockDto = StockMapper.INSTANCE.entityToDto(tuple.getT1());
        createdStockDto.setProductName(tuple.getT2().getName());
        return createdStockDto;
      })
      .doOnNext(createdStockDto -> log.debug(createdStockDto.toString()));
  }
  

  
  @Override
  public Mono<List<StockTransactionDto>> findTransactions(Integer productId) {
    return Mono.just(Optional.ofNullable(productId))
      .flatMap( optional -> {
          if (optional.isPresent()) {
            return this.findTransactionsByProductId(optional.get());
          }
          return this.findAllTransactions();
        }
      );
  }
  
  @Override
  public Mono<List<StockTransactionDto>> findTransactionsByProductId(Integer productId) {
    log.debug("findTransactionsByProductId");
    return this.productRepository.getProductById(productId)
      .flatMapMany(productDto -> this.transactionRepository.findAllByProductId(productDto.getId())
        .map(StockTransactionMapper.INSTANCE::entityToDto))
      .doOnNext(transactionDto -> log.debug(transactionDto.toString()))
      .collectList();
  }
  
  @Override
  public Mono<List<StockTransactionDto>> findAllTransactions() {
    log.debug("findAllTransactions");
    return this.transactionRepository.findAll()
      .map(StockTransactionMapper.INSTANCE::entityToDto)
      .doOnNext(transactionDto -> log.debug(transactionDto.toString()))
      .collectList();
  }
  
  @Override
  @Transactional
  public Mono<StockTransactionDto> createTransaction(Integer productId, StockTransactionDto transactionDto) {
    log.debug("createTransaction");
    return Mono.zip(
        Mono.from(this.stockRepository.findById(productId)
          .switchIfEmpty(Mono.error(new StockNotFoundException(productId)))),
        Mono.just(transactionDto))
      .map(tuple -> {
        StockTransaction transaction = StockTransactionHelper.create(tuple.getT1(),tuple.getT2());
        return Tuples.of(tuple.getT1(), transaction);
      })
      .flatMap(tuple ->
        Mono.zip(
          Mono.just(tuple.getT1()),
          Mono.from(this.transactionRepository.save(tuple.getT2()))))
      .map(tuple -> {
          Stock stock = StockTransactionHelper.getUpdatedStock(tuple.getT1(),tuple.getT2());
          return Tuples.of(stock, tuple.getT2());
      })
      .flatMap(tuple -> Mono.zip(
        Mono.from(this.stockRepository.save(tuple.getT1())),
        Mono.just(tuple.getT2())))
      .map(Tuple2::getT2)
      .map(StockTransactionMapper.INSTANCE::entityToDto)
      .doOnNext(stockTransactionDto -> log.debug(stockTransactionDto.toString()));
  }

  
}
