package com.jpdr.apps.demo.webflux.stock.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Table("stock")
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Stock implements Persistable<Integer> {
  
  @Id
  @Column("product_id")
  Integer productId;
  @Column("quantity")
  Integer quantity;
  @Column("unit_price")
  BigDecimal unitPrice;
  @Column("last_transaction_id")
  Integer lastTransactionId;
  @Column("last_transaction_date")
  OffsetDateTime lastTransactionDate;

  @Transient
  @Default
  boolean isNewEntity = false;

  @Override
  public Integer getId() {
    return this.productId;
  }

  @Override
  public boolean isNew() {
    return this.isNewEntity;
  }
}
