package com.jpdr.apps.demo.webflux.stock.exception.stock;

public class StockNotFoundException extends RuntimeException{
  
  public StockNotFoundException(Integer productId){
    super("There is no stock for product " + productId);
  }
  
}
