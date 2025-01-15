package com.jpdr.apps.demo.webflux.stock.helper;

import com.jpdr.apps.demo.webflux.stock.exception.stock.InsufficientQuantityException;
import com.jpdr.apps.demo.webflux.stock.model.Stock;
import com.jpdr.apps.demo.webflux.stock.model.StockTransaction;
import com.jpdr.apps.demo.webflux.stock.service.dto.stock.StockTransactionDto;
import com.jpdr.apps.demo.webflux.stock.service.enums.StockTransactionTypeEnum;
import com.jpdr.apps.demo.webflux.stock.service.mapper.CloningMapper;
import com.jpdr.apps.demo.webflux.stock.service.mapper.StockTransactionMapper;
import jakarta.validation.ValidationException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class StockTransactionHelper {
  
  private StockTransactionHelper() {}
  
  public static StockTransaction create(Stock stock, StockTransactionDto dto){
    
      switch (dto.getTransactionType()){
        case StockTransactionTypeEnum.INCREASE : {
          if (dto.getQuantity() < 0){
            throw new ValidationException("The quantity is invalid.");
          }
          StockTransaction stockTransaction = getBaseTransaction(stock.getProductId(), dto);
          stockTransaction.setUnitPrice(BigDecimal.ZERO);
          return stockTransaction;
        }
        
        case StockTransactionTypeEnum.DECREASE : {
          if ( dto.getQuantity() < 0){
            throw new ValidationException("The quantity is invalid.");
          }
          if(stock.getQuantity() < dto.getQuantity()){
            throw new InsufficientQuantityException(stock.getProductId(), stock.getQuantity());
          }
          StockTransaction stockTransaction = getBaseTransaction(stock.getProductId(), dto);
          stockTransaction.setUnitPrice(BigDecimal.ZERO);
          return stockTransaction;
        }
        case StockTransactionTypeEnum.SET_PRICE : {
          if (dto.getUnitPrice().compareTo(BigDecimal.ZERO) < 1){
            throw new ValidationException("The unit price is invalid.");
          }
          StockTransaction stockTransaction = getBaseTransaction(stock.getProductId(), dto);
          stockTransaction.setQuantity(0);
          return stockTransaction;
        }
        default:
          throw new ValidationException("A stock transaction value is invalid.");
      }
  }
  
  public static Stock getUpdatedStock(Stock stock, StockTransaction savedTransaction) {
    Stock updatedStock = CloningMapper.INSTANCE.clone(stock);
    
    switch (StockTransactionTypeEnum.fromValue(savedTransaction.getTransactionType()) ) {
      case StockTransactionTypeEnum.INCREASE: {
        updatedStock.setQuantity(stock.getQuantity() + savedTransaction.getQuantity());
        break;
      }
      case StockTransactionTypeEnum.DECREASE: {
        updatedStock.setQuantity(stock.getQuantity() - savedTransaction.getQuantity());
        break;
      }
      case StockTransactionTypeEnum.SET_PRICE: {
        updatedStock.setUnitPrice(savedTransaction.getUnitPrice());
        break;
      }
    }
    updatedStock.setLastTransactionId(savedTransaction.getId());
    updatedStock.setLastTransactionDate(savedTransaction.getTransactionDate());
    return updatedStock;
  }
  
  
  private static StockTransaction getBaseTransaction(Long productId, StockTransactionDto dto){
    StockTransaction transaction = StockTransactionMapper.INSTANCE.dtoToEntity(dto);
    transaction.setProductId(productId);
    transaction.setTransactionType(dto.getTransactionType().getValue());
    transaction.setTransactionDate(OffsetDateTime.now());
    transaction.setUnitPrice(dto.getUnitPrice());
    transaction.setQuantity(dto.getQuantity());
    return transaction;
  }
  
  
  
}
