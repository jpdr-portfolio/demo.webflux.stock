package com.jpdr.apps.demo.webflux.stock.service.mapper;

import com.jpdr.apps.demo.webflux.stock.model.Stock;
import com.jpdr.apps.demo.webflux.stock.model.StockTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.control.DeepClone;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", mappingControl = DeepClone.class)

public interface CloningMapper {
  
  CloningMapper INSTANCE = Mappers.getMapper(CloningMapper.class);
  
  @Mapping(target = "isNewEntity", ignore = true)
  Stock clone(Stock entity);
  StockTransaction clone(StockTransaction entity);
  
}
