package com.jpdr.apps.demo.webflux.stock.service.dto.stock;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockDto {
  
  @JsonInclude(Include.NON_NULL)
  Integer productId;
  @JsonInclude(Include.NON_NULL)
  String productName;
  @NonNull
  Integer quantity;
  @NonNull
  BigDecimal unitPrice;
  @JsonInclude(Include.NON_NULL)
  Integer lastTransactionId;
  @JsonInclude(Include.NON_EMPTY)
  String lastTransactionDate;
  
}