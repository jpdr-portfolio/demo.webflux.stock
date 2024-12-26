package com.jpdr.apps.demo.webflux.stock.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpdr.apps.demo.webflux.stock.service.dto.product.ProductDto;
import com.jpdr.apps.demo.webflux.stock.service.dto.stock.StockDto;
import com.jpdr.apps.demo.webflux.stock.util.DtoSerializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@EnableCaching
@Configuration
public class CacheConfig {
  
  @Bean
  public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper){
    
    ObjectMapper mapper = objectMapper.copy()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
    mapper.findAndRegisterModules();
    
    DtoSerializer<StockDto> stockDtoDtoSerializer = new DtoSerializer<>(mapper, StockDto.class);
    DtoSerializer<ProductDto> productDtoDtoSerializer = new DtoSerializer<>(mapper, ProductDto.class);
    
    RedisSerializationContext.SerializationPair<StockDto> stockDtoSerializationPair =
      RedisSerializationContext.SerializationPair.fromSerializer(stockDtoDtoSerializer);
    RedisSerializationContext.SerializationPair<ProductDto> productDtoSerializationPair =
      RedisSerializationContext.SerializationPair.fromSerializer(productDtoDtoSerializer);
    
    RedisCacheConfiguration stockCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
      .serializeValuesWith(stockDtoSerializationPair);
    RedisCacheConfiguration productCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
      .serializeValuesWith(productDtoSerializationPair);
    
    return RedisCacheManager.builder(redisConnectionFactory)
      .withCacheConfiguration("stock",stockCacheConfiguration)
      .withCacheConfiguration("products", productCacheConfiguration)
      .build();
  }

}
