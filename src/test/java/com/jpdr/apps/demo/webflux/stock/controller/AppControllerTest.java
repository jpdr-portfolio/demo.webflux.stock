package com.jpdr.apps.demo.webflux.stock.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpdr.apps.demo.webflux.stock.service.AppService;
import com.jpdr.apps.demo.webflux.stock.service.dto.stock.StockDto;
import com.jpdr.apps.demo.webflux.stock.service.dto.stock.StockTransactionDto;
import com.jpdr.apps.demo.webflux.stock.service.enums.StockTransactionTypeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static com.jpdr.apps.demo.webflux.stock.util.TestDataGenerator.getAllStockDtos;
import static com.jpdr.apps.demo.webflux.stock.util.TestDataGenerator.getNewStockDto;
import static com.jpdr.apps.demo.webflux.stock.util.TestDataGenerator.getNewTransactionDto;
import static com.jpdr.apps.demo.webflux.stock.util.TestDataGenerator.getStockDto;
import static com.jpdr.apps.demo.webflux.stock.util.TestDataGenerator.getStockTransactionDto;
import static com.jpdr.apps.demo.webflux.stock.util.TestDataGenerator.getStockTransactionDtos;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ExtendWith(MockitoExtension.class)
class AppControllerTest {
  
  @Autowired
  private WebTestClient webTestClient;
  @MockBean
  private AppService appService;
  @Autowired
  private ObjectMapper objectMapper;
  
  @Test
  @DisplayName("OK - Find All Stock")
  void givenStockWhenFindAllStockThenReturnStock() throws JsonProcessingException {
  
    List<StockDto> expectedStock = getAllStockDtos();
    String expectedBody = objectMapper.writeValueAsString(expectedStock);
    
    when(appService.findAllStocks()).thenReturn(Mono.just(expectedStock));
    
    
    FluxExchangeResult<String> exchangeResult = this.webTestClient.get()
      .uri("/stock")
      .exchange()
      .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
        .isOk()
      .returnResult(String.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedBody -> assertEquals(expectedBody, receivedBody))
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("OK - Find Stock By Product Id")
  void givenProductIdWhenFindStockByProductIdThenReturnStock() {
  
    StockDto expectedDto = getStockDto();
    
    when(appService.findStockByProductId(anyInt()))
      .thenReturn(Mono.just(expectedDto));
    
    FluxExchangeResult<StockDto> exchangeResult = this.webTestClient.get()
      .uri("/stock" + "/" + 1)
      .exchange()
      .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
        .isOk()
      .returnResult(StockDto.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedDto -> assertEquals(expectedDto, receivedDto))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Create Stock")
  void givenStockWhenCreateStockThenReturnStock() {
  
    StockDto requestDto = getNewStockDto();
    StockDto expectedDto = getStockDto();
    
    when(appService.createStock(any(StockDto.class)))
      .thenReturn(Mono.just(expectedDto));
    
    FluxExchangeResult<StockDto> exchangeResult = this.webTestClient.post()
      .uri("/stock")
      .bodyValue(requestDto)
      .exchange()
      .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
        .isCreated()
      .returnResult(StockDto.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedDto -> assertEquals(expectedDto, receivedDto))
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("OK - Find Transactions By Product Id")
  void givenTransactionsWhenFindTransactionsByProductIdThenReturnTransactions() throws JsonProcessingException {
    
    List<StockTransactionDto> expectedDtos = getStockTransactionDtos();
    String expectedBody = objectMapper.writeValueAsString(expectedDtos);
    
    when(appService.findTransactions(anyInt())).thenReturn(Mono.just(expectedDtos));
    
    FluxExchangeResult<String> exchangeResult = this.webTestClient.get().uri("/stock" + "/" + 1 + "/" + "transactions").exchange().expectHeader().contentType(MediaType.APPLICATION_JSON).expectStatus().isOk().returnResult(String.class);
    
    StepVerifier.create(exchangeResult.getResponseBody()).assertNext(receivedBody -> assertEquals(expectedBody, receivedBody)).expectComplete().verify();
  }
  
  @Test
  @DisplayName("OK - Create Transaction - Increase Stock")
  void givenTransactionWhenIncreaseStockThenReturnTransaction() {
  
    StockTransactionDto requestDto = getNewTransactionDto();
    StockTransactionDto expectedDto = getStockTransactionDto();
    
    when(appService.createTransaction(anyInt(),any(StockTransactionDto.class)))
      .thenReturn(Mono.just(expectedDto));
    
    FluxExchangeResult<StockTransactionDto> exchangeResult = this.webTestClient.post()
      .uri("/stock" + "/" + 1 + "/" + "transactions" )
      .bodyValue(requestDto)
      .exchange()
      .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
        .isCreated()
      .returnResult(StockTransactionDto.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedDto -> assertEquals(expectedDto, receivedDto))
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("OK - Create Transaction - Decrease Stock")
  void givenTransactionWhenDecreaseStockThenReturnTransaction() {
    
    StockTransactionDto requestDto = getNewTransactionDto();
    requestDto.setTransactionType(StockTransactionTypeEnum.DECREASE);
    StockTransactionDto expectedDto = getStockTransactionDto();
    expectedDto.setTransactionType(StockTransactionTypeEnum.DECREASE);
    
    when(appService.createTransaction(anyInt(),any(StockTransactionDto.class)))
      .thenReturn(Mono.just(expectedDto));
    
    FluxExchangeResult<StockTransactionDto> exchangeResult = this.webTestClient.post()
      .uri("/stock" + "/" + 1 + "/" + "transactions" )
      .bodyValue(requestDto)
      .exchange()
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
      .isCreated()
      .returnResult(StockTransactionDto.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedDto -> assertEquals(expectedDto, receivedDto))
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("OK - Create Transaction - Set Price")
  void givenTransactionWhenSetPriceThenReturnTransaction() {
    
    StockTransactionDto requestDto = getNewTransactionDto();
    requestDto.setTransactionType(StockTransactionTypeEnum.SET_PRICE);
    requestDto.setUnitPrice(new BigDecimal(300.00));
    requestDto.setQuantity(0);
    StockTransactionDto expectedDto = getStockTransactionDto();
    expectedDto.setTransactionType(StockTransactionTypeEnum.SET_PRICE);
    
    when(appService.createTransaction(anyInt(),any(StockTransactionDto.class)))
      .thenReturn(Mono.just(expectedDto));
    
    FluxExchangeResult<StockTransactionDto> exchangeResult = this.webTestClient.post()
      .uri("/stock" + "/" + 1 + "/" + "transactions" )
      .bodyValue(requestDto)
      .exchange()
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
      .isCreated()
      .returnResult(StockTransactionDto.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedDto -> assertEquals(expectedDto, receivedDto))
      .expectComplete()
      .verify();
    
  }
  
  
}
