package com.jpdr.apps.demo.webflux.stock.util;

import com.jpdr.apps.demo.webflux.stock.model.Stock;
import com.jpdr.apps.demo.webflux.stock.model.StockTransaction;
import com.jpdr.apps.demo.webflux.stock.service.dto.product.ProductDto;
import com.jpdr.apps.demo.webflux.stock.service.dto.stock.StockDto;
import com.jpdr.apps.demo.webflux.stock.service.dto.stock.StockTransactionDto;
import com.jpdr.apps.demo.webflux.stock.service.enums.StockTransactionTypeEnum;
import com.jpdr.apps.demo.webflux.stock.service.mapper.StockMapper;
import com.jpdr.apps.demo.webflux.stock.service.mapper.StockTransactionMapper;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class TestDataGenerator {
  
  public static final String CREATION_DATE = "2024-10-14T10:39:45.732446-03:00";
  public static final String PRODUCT_NAME = "Product Name";
  public static final String CATEGORY_NAME = "Category Name";
  public static final String RETAILER_NAME = "Retailer Name";
  public static final String DESCRIPTION = "Description";
  
  public static ProductDto getProduct(){
    return getProduct(1);
  }
  
  public static ProductDto getProduct(long productId){
    return ProductDto.builder()
      .id(productId)
      .productName(PRODUCT_NAME + " " + productId)
      .categoryId(null)
      .categoryName(CATEGORY_NAME)
      .retailerId(null)
      .retailerName(RETAILER_NAME)
      .isActive(true)
      .creationDate(CREATION_DATE)
      .deletionDate(null)
      .build();
  }
  
  public static StockTransactionDto getNewTransactionDto(){
    return StockTransactionDto.builder()
      .id(null)
      .transactionDate(null)
      .transactionType(StockTransactionTypeEnum.INCREASE)
      .productId(1L)
      .quantity(100)
      .unitPrice(BigDecimal.ZERO)
      .build();
  }
  
  
  public static StockDto getNewStockDto(){
    return StockDto.builder()
      .productId(1L)
      .quantity(0)
      .unitPrice(BigDecimal.ZERO)
      .productName(null)
      .lastTransactionId(null)
      .lastTransactionDate(null)
      .build();
  }
  
  
  public static List<StockDto> getAllStockDtos(){
    return getList(TestDataGenerator::getStockDto);
  }
  
  public static StockDto getStockDto(){
    return getStockDto(1);
  }
  
  public static StockDto getStockDto(long productId){
    StockDto stockDto = StockMapper.INSTANCE.entityToDto(getStock(productId));
    stockDto.setProductName(PRODUCT_NAME);
    return stockDto;
  }
  
  
  public static List<Stock> getAllStock(){
    return getList(TestDataGenerator::getStock);
  }
  
  public static Stock getStock(){
    return getStock(1);
  }
  
  public static Stock getStock(long productId){
    return Stock.builder()
      .productId(productId)
      .quantity(100)
      .unitPrice(BigDecimal.valueOf(100.00))
      .lastTransactionId(1L)
      .lastTransactionDate(OffsetDateTime.parse(CREATION_DATE))
      .build();
  }
  
  public static List<StockTransactionDto> getStockTransactionDtos(){
    return getList(TestDataGenerator::getStockTransactionDto);
  }
  
  
  public static StockTransactionDto getStockTransactionDto(){
    return StockTransactionMapper.INSTANCE.entityToDto(getStockTransaction(1,
      StockTransactionTypeEnum.INCREASE));
  }
  
  public static StockTransactionDto getStockTransactionDto(long transactionId){
    return StockTransactionMapper.INSTANCE.entityToDto(getStockTransaction(transactionId,
      StockTransactionTypeEnum.INCREASE));
  }
  
  
  public static StockTransactionDto getStockTransactionDto(StockTransactionTypeEnum type){
    StockTransactionDto transactionDto = StockTransactionMapper
      .INSTANCE.entityToDto(getStockTransaction(1,type));
    transactionDto.setId(1L);
    transactionDto.setProductId(1L);
    return transactionDto;
  }
  
  
  public static StockTransactionDto getStockTransactionDto(long transactionId, StockTransactionTypeEnum type){
    return StockTransactionMapper.INSTANCE.entityToDto(getStockTransaction(transactionId,type));
  }
  
  
  public static List<StockTransaction> getStockTransactions(){
    return getList(TestDataGenerator::getStockTransaction);
  }
  
  
  public static StockTransaction getStockTransaction(){
    return getStockTransaction(1L, StockTransactionTypeEnum.INCREASE);
  }
  
  public static StockTransaction getStockTransaction(long transactionId){
    return getStockTransaction(transactionId, StockTransactionTypeEnum.INCREASE);
  }
  
  public static StockTransaction getStockTransaction(StockTransactionTypeEnum type){
    return getStockTransaction(1L, type);
  }
  
  public static StockTransaction getStockTransaction(long transactionId, StockTransactionTypeEnum type){
    return StockTransaction.builder()
      .id(transactionId)
      .productId(1L)
      .quantity(100)
      .description(DESCRIPTION)
      .unitPrice(BigDecimal.ZERO)
      .transactionDate(OffsetDateTime.parse(CREATION_DATE))
      .transactionType(type.getValue())
      .build();
  }
  
  
  private static <R> List<R> getList(Function<Integer,R> function){
    return Stream.iterate(1, n -> n + 1)
      .limit(3)
      .map(function)
      .toList();
  }
  
  
  
  
  
}
