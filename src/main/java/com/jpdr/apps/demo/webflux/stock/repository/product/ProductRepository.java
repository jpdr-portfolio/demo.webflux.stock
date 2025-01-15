package com.jpdr.apps.demo.webflux.stock.repository.product;

import com.jpdr.apps.demo.webflux.stock.service.dto.product.ProductDto;
import reactor.core.publisher.Mono;

public interface ProductRepository {
  
  Mono<ProductDto> getProductById(Long productId);
  
}
