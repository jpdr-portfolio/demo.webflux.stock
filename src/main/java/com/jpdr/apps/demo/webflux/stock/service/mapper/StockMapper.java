package com.jpdr.apps.demo.webflux.stock.service.mapper;

import com.jpdr.apps.demo.webflux.stock.model.Stock;
import com.jpdr.apps.demo.webflux.stock.service.dto.stock.StockDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = java.util.Objects.class)
public interface StockMapper {
  
  StockMapper INSTANCE = Mappers.getMapper(StockMapper.class);
  
  @Mapping(target = "isNewEntity", ignore = true)
  @Mapping(target = "lastTransactionDate", expression = "java(null)")
  Stock dtoToEntity(StockDto dto);
  
  @Mapping(target = "productName", expression = "java(null)")
  @Mapping(target = "lastTransactionDate", expression = "java(Objects.toString(entity.getLastTransactionDate(),null))" )
  StockDto entityToDto(Stock entity);
  
}
