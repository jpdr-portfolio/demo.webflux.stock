package com.jpdr.apps.demo.webflux.stock.service.dto.stock;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.jpdr.apps.demo.webflux.stock.service.enums.StockTransactionTypeEnum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockTransactionDto {
  
  @JsonInclude(Include.NON_NULL)
  Integer id;
  @JsonInclude(Include.NON_NULL)
  Integer productId;
  @JsonInclude(Include.NON_NULL)
  String description;
  @NonNull
  Integer quantity;
  @NonNull
  BigDecimal unitPrice;
  @NonNull
  StockTransactionTypeEnum transactionType;
  @JsonInclude(Include.NON_EMPTY)
  String transactionDate;
  
}
