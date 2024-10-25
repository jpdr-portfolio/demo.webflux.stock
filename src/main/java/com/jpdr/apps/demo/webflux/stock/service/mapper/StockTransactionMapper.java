package com.jpdr.apps.demo.webflux.stock.service.mapper;

import com.jpdr.apps.demo.webflux.stock.model.StockTransaction;
import com.jpdr.apps.demo.webflux.stock.service.dto.stock.StockTransactionDto;
import com.jpdr.apps.demo.webflux.stock.service.enums.StockTransactionTypeEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = {java.util.Objects.class, StockTransactionTypeEnum.class})
public interface StockTransactionMapper {
  
  StockTransactionMapper INSTANCE = Mappers.getMapper(StockTransactionMapper.class);
  
  @Mapping(target = "id", expression = "java(null)")
  @Mapping(target = "transactionDate", expression = "java(null)")
  @Mapping(target = "transactionType", expression = "java(dto.getTransactionType().getValue())" )
  StockTransaction dtoToEntity(StockTransactionDto dto);
  
  @Mapping(target = "transactionDate", expression = "java(Objects.toString(entity.getTransactionDate(),null))" )
  @Mapping(target = "transactionType", expression = "java(StockTransactionTypeEnum.fromValue(entity.getTransactionType()))" )
  StockTransactionDto entityToDto(StockTransaction entity);
  
}
