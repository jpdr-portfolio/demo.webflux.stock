package com.jpdr.apps.demo.webflux.stock.model;

import com.jpdr.apps.demo.webflux.stock.service.enums.StockTransactionTypeEnum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Table("stock_transaction")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockTransaction {
  
  @Id
  @Column("id")
  Integer id;
  @Column("product_id")
  Integer productId;
  @Column("description")
  String description;
  @Column("quantity")
  Integer quantity;
  @Column("unit_price")
  BigDecimal unitPrice;
  @Column("transaction_type")
  String transactionType;
  @Column("transaction_date")
  OffsetDateTime transactionDate;
  
}
