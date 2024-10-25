package com.jpdr.apps.demo.webflux.stock.service;

import com.jpdr.apps.demo.webflux.stock.exception.product.ProductNotFoundException;
import com.jpdr.apps.demo.webflux.stock.exception.stock.InsufficientQuantityException;
import com.jpdr.apps.demo.webflux.stock.exception.stock.StockNotFoundException;
import com.jpdr.apps.demo.webflux.stock.model.Stock;
import com.jpdr.apps.demo.webflux.stock.model.StockTransaction;
import com.jpdr.apps.demo.webflux.stock.repository.product.ProductRepository;
import com.jpdr.apps.demo.webflux.stock.repository.stock.StockRepository;
import com.jpdr.apps.demo.webflux.stock.repository.stock.StockTransactionRepository;
import com.jpdr.apps.demo.webflux.stock.service.dto.product.ProductDto;
import com.jpdr.apps.demo.webflux.stock.service.dto.stock.StockDto;
import com.jpdr.apps.demo.webflux.stock.service.dto.stock.StockTransactionDto;
import com.jpdr.apps.demo.webflux.stock.service.enums.StockTransactionTypeEnum;
import com.jpdr.apps.demo.webflux.stock.service.impl.AppServiceImpl;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.jpdr.apps.demo.webflux.stock.util.TestDataGenerator.getAllStock;
import static com.jpdr.apps.demo.webflux.stock.util.TestDataGenerator.getNewStockDto;
import static com.jpdr.apps.demo.webflux.stock.util.TestDataGenerator.getProduct;
import static com.jpdr.apps.demo.webflux.stock.util.TestDataGenerator.getStock;
import static com.jpdr.apps.demo.webflux.stock.util.TestDataGenerator.getStockTransactionDto;
import static com.jpdr.apps.demo.webflux.stock.util.TestDataGenerator.getStockTransactions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AppServiceTest {
  
  @InjectMocks
  private AppServiceImpl appService;
  
  @Mock
  private StockRepository stockRepository;
  
  @Mock
  private StockTransactionRepository stockTransactionRepository;
  
  @Mock
  private ProductRepository productRepository;
  
  private ProductDto product;
  
  @BeforeEach
  void setupEach(){
    product = getProduct();
    when(productRepository.getProductById(anyInt()))
      .thenReturn(Mono.just(product));
  }
  
  
  @Test
  @DisplayName("OK - Find All Stock")
  void givenStockWhenFindAllStockThenReturnStock(){
    
    List<Stock> expectedStock = getAllStock();
    Map<Integer, Stock> expectedStockMap = expectedStock.stream()
      .collect(Collectors.toMap(Stock::getProductId, Function.identity()));
    
    when(stockRepository.findAll())
      .thenReturn(Flux.fromIterable(expectedStock));
    
    StepVerifier.create(appService.findAllStocks())
      .assertNext(receivedStock -> assertStock(expectedStockMap.get(receivedStock.getProductId()),
        receivedStock))
      .assertNext(receivedStock -> assertStock(expectedStockMap.get(receivedStock.getProductId()),
        receivedStock))
      .assertNext(receivedStock -> assertStock(expectedStockMap.get(receivedStock.getProductId()),
        receivedStock))
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("OK - Find Stock By Id")
  void givenStockWhenFindStockByIdThenReturnStock(){
    
    Stock expectedStock = getStock();
    
    when(stockRepository.findById(anyInt()))
      .thenReturn(Mono.just(expectedStock));
    
    StepVerifier.create(appService.findStockByProductId(1))
      .assertNext(receivedStock -> assertStock(expectedStock, receivedStock))
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("Error - Find Stock By Id - Not found")
  void givenNotFoundWhenFindStockByIdThenReturnError(){
    
    when(stockRepository.findById(anyInt()))
      .thenReturn(Mono.empty());
    
    StepVerifier.create(appService.findStockByProductId(1))
      .expectError(StockNotFoundException.class)
      .verify();
    
  }
  
  @Test
  @DisplayName("Error - Find Stock By Id - Product Not found")
  void givenProductNotFoundWhenFindStockByIdThenReturnError(){
    
    when(productRepository.getProductById(anyInt()))
      .thenReturn(Mono.error(new ProductNotFoundException(1, new RuntimeException())));
    
    StepVerifier.create(appService.findStockByProductId(1))
      .expectError(ProductNotFoundException.class)
      .verify();
    
  }
  
  @Test
  @DisplayName("OK - Create Stock")
  void givenStockWhenCreateStockThenReturnStock(){
    
    StockDto requestStock = getNewStockDto();
    Stock expectedStock = getStock();
    expectedStock.setQuantity(0);
    expectedStock.setLastTransactionId(null);
    expectedStock.setLastTransactionDate(null);
    
    when(stockRepository.save(any(Stock.class)))
      .thenAnswer(i->{
        Stock savedStock = i.getArgument(0);
        savedStock.setProductId(1);
        return Mono.just(savedStock);
      });
    
    StepVerifier.create(appService.createStock(requestStock))
      .assertNext(receivedStock -> assertEquals(expectedStock.getProductId(),
        receivedStock.getProductId())
      )
      .expectComplete()
      .verify();
    
  }
  
  
  @Test
  @DisplayName("Error - Create Stock - Product Not Found")
  void givenProductNotFoundWhenCreateStockThenReturnError(){
    
    StockDto requestStock = getNewStockDto();
    
    when(productRepository.getProductById(anyInt()))
      .thenReturn(Mono.error(new ProductNotFoundException(1,new RuntimeException())));
    
    StepVerifier.create(appService.createStock(requestStock))
      .expectError(ProductNotFoundException.class)
      .verify();
    
  }
  
  
  @Test
  @DisplayName("OK - Find Transactions By Product Id")
  void givenProductIdWhenFindTransactionsByProductIdThenReturnTransactions(){
    
    List<StockTransaction> expectedTransactions = getStockTransactions();
    Map<Integer, StockTransaction> expectedTransactionsMap = expectedTransactions.stream()
      .collect(Collectors.toMap(StockTransaction::getId, Function.identity()));
    
    when(stockTransactionRepository.findAllByProductId(anyInt()))
      .thenReturn(Flux.fromIterable(expectedTransactions));
    
    StepVerifier.create(appService.findTransactions(1))
      .assertNext(receivedTransaction -> assertTransaction(expectedTransactionsMap.get(receivedTransaction.getId()),
        receivedTransaction))
      .assertNext(receivedTransaction -> assertTransaction(expectedTransactionsMap.get(receivedTransaction.getId()),
        receivedTransaction))
      .assertNext(receivedTransaction -> assertTransaction(expectedTransactionsMap.get(receivedTransaction.getId()),
        receivedTransaction))
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("Error - Find Transactions By Product Id - Product Not Found")
  void givenProductNotFoundWhenFindTransactionsByProductIdThenReturnError(){
    
    when(productRepository.getProductById(anyInt()))
      .thenReturn(Mono.error(new ProductNotFoundException(1,new RuntimeException())));
    
    StepVerifier.create(appService.findTransactions(1))
      .expectError(ProductNotFoundException.class)
      .verify();
    
  }
  
  
  @Test
  @DisplayName("OK - Create Transaction - Increase Stock")
  void givenTransactionWhenIncreaseStockThenReturnTransaction(){
    
    StockTransactionDto requestTransaction = getStockTransactionDto(StockTransactionTypeEnum.INCREASE);
    Stock expectedStock = getStock();
    expectedStock.setProductId(1);
    
    ArgumentCaptor<Stock> stockCaptor = ArgumentCaptor.forClass(Stock.class);
    
    when(stockRepository.findById(anyInt()))
      .thenReturn(Mono.just(expectedStock));
    when(stockTransactionRepository.save(any(StockTransaction.class)))
      .thenAnswer(i->{
        StockTransaction savedTransaction = i.getArgument(0);
        savedTransaction.setId(1);
        savedTransaction.setProductId(1);
        return Mono.just(savedTransaction);
      });
    when(stockRepository.save(any(Stock.class)))
      .thenAnswer(i->{
        Stock savedStock = i.getArgument(0);
        savedStock.setProductId(1);
        return Mono.just(savedStock);
      });
    
    StepVerifier.create(appService.createTransaction(1, requestTransaction))
      .assertNext(receivedTransaction -> {
        verify(stockRepository).save(stockCaptor.capture());
        assertEquals(200, stockCaptor.getValue().getQuantity());
        assertEquals(expectedStock.getUnitPrice(), stockCaptor.getValue().getUnitPrice());
      })
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("Error - Create Transaction - Increase Stock - Stock Not Found")
  void givenStockNotFoundWhenIncreaseStockThenReturnError(){
    
    StockTransactionDto requestTransaction = getStockTransactionDto(StockTransactionTypeEnum.INCREASE);
    
    when(stockRepository.findById(anyInt()))
      .thenReturn(Mono.error(new StockNotFoundException(1)));
    
    StepVerifier.create(appService.createTransaction(1, requestTransaction))
      .expectError(StockNotFoundException.class)
      .verify();
    
  }
  
  
  
  
  
  
  @Test
  @DisplayName("OK - Create Transaction - Decrease Stock")
  void givenTransactionWhenDecreaseStockThenReturnTransaction(){
    
    StockTransactionDto requestTransaction = getStockTransactionDto(StockTransactionTypeEnum.DECREASE);
    Stock expectedStock = getStock();
    expectedStock.setProductId(1);
    
    ArgumentCaptor<Stock> stockCaptor = ArgumentCaptor.forClass(Stock.class);
    
    when(stockRepository.findById(anyInt()))
      .thenReturn(Mono.just(expectedStock));
    when(stockTransactionRepository.save(any(StockTransaction.class)))
      .thenAnswer(i->{
        StockTransaction savedTransaction = i.getArgument(0);
        savedTransaction.setId(1);
        savedTransaction.setProductId(1);
        return Mono.just(savedTransaction);
      });
    when(stockRepository.save(any(Stock.class)))
      .thenAnswer(i->{
        Stock savedStock = i.getArgument(0);
        savedStock.setProductId(1);
        return Mono.just(savedStock);
      });
    
    StepVerifier.create(appService.createTransaction(1, requestTransaction))
      .assertNext(receivedTransaction -> {
        verify(stockRepository).save(stockCaptor.capture());
        assertEquals(0, stockCaptor.getValue().getQuantity());
        assertEquals(expectedStock.getUnitPrice(), stockCaptor.getValue().getUnitPrice());
      })
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("Error - Create Transaction - Decrease Stock - Stock Not Found")
  void givenStockNotFoundWhenDecreaseStockThenReturnError(){
    
    StockTransactionDto requestTransaction = getStockTransactionDto(StockTransactionTypeEnum.DECREASE);
    
    when(stockRepository.findById(anyInt()))
      .thenReturn(Mono.error(new StockNotFoundException(1)));
    
    StepVerifier.create(appService.createTransaction(1, requestTransaction))
      .expectError(StockNotFoundException.class)
      .verify();
    
  }
  
  @Test
  @DisplayName("Error - Create Transaction - Stock Not Found")
  void givenStockNotFoundWhenCreateTransactionThenReturnError(){
    
    StockTransactionDto requestTransaction = getStockTransactionDto(StockTransactionTypeEnum.DECREASE);
    
    when(stockRepository.findById(anyInt()))
      .thenReturn(Mono.empty());
    
    StepVerifier.create(appService.createTransaction(1, requestTransaction))
      .expectError(StockNotFoundException.class)
      .verify();
    
  }
  
  
  @Test
  @DisplayName("Error - Create Transaction - Decrease Stock - Insufficient quantity")
  void givenInsufficientQuantityWhenDecreaseStockThenReturnError(){
    
    StockTransactionDto requestTransaction = getStockTransactionDto(StockTransactionTypeEnum.DECREASE);
    Stock expectedStock = getStock();
    expectedStock.setQuantity(0);
    
    when(stockRepository.findById(anyInt()))
      .thenReturn(Mono.just(expectedStock));
    
    StepVerifier.create(appService.createTransaction(1, requestTransaction))
      .expectError(InsufficientQuantityException.class)
      .verify();
    
  }
  
  
  
  
  
  
  
  
  
  
  
  
  @Test
  @DisplayName("OK - Create Transaction - Set Price")
  void givenTransactionWhenSetPriceThenReturnTransaction(){
    
    StockTransactionDto requestTransaction = getStockTransactionDto(StockTransactionTypeEnum.SET_PRICE);
    requestTransaction.setQuantity(0);
    requestTransaction.setUnitPrice(BigDecimal.valueOf(500.00));

    Stock expectedStock = getStock();
    expectedStock.setProductId(1);
    expectedStock.setQuantity(0);
    expectedStock.setUnitPrice(BigDecimal.valueOf(100.00));
    
    ArgumentCaptor<Stock> stockCaptor = ArgumentCaptor.forClass(Stock.class);
    
    when(stockRepository.findById(anyInt()))
      .thenReturn(Mono.just(expectedStock));
    when(stockTransactionRepository.save(any(StockTransaction.class)))
      .thenAnswer(i->{
        StockTransaction savedTransaction = i.getArgument(0);
        savedTransaction.setId(1);
        savedTransaction.setProductId(1);
        return Mono.just(savedTransaction);
      });
    when(stockRepository.save(any(Stock.class)))
      .thenAnswer(i->{
        Stock savedStock = i.getArgument(0);
        savedStock.setProductId(1);
        return Mono.just(savedStock);
      });
    
    StepVerifier.create(appService.createTransaction(1, requestTransaction))
      .assertNext(receivedTransaction -> {
        verify(stockRepository).save(stockCaptor.capture());
        assertEquals(expectedStock.getQuantity(), stockCaptor.getValue().getQuantity());
        assertEquals(BigDecimal.valueOf(500.00), stockCaptor.getValue().getUnitPrice());
      })      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("Error - Create Transaction - Set Price - Stock Not Found")
  void givenStockNotFoundWhenSetPriceThenReturnError(){
    
    StockTransactionDto requestTransaction = getStockTransactionDto(StockTransactionTypeEnum.SET_PRICE);
    requestTransaction.setQuantity(0);
    requestTransaction.setUnitPrice(BigDecimal.valueOf(100.00));
    
    when(stockRepository.findById(anyInt()))
      .thenReturn(Mono.error(new StockNotFoundException(1)));
    
    StepVerifier.create(appService.createTransaction(1, requestTransaction))
      .expectError(StockNotFoundException.class)
      .verify();
    
  }
  
  
  @Test
  @DisplayName("Error - Create Transaction - Set Price - Price Below Zero")
  void givenPriceBelowZeroWhenSetPriceThenReturnError(){
    
    StockTransactionDto requestTransaction = getStockTransactionDto(StockTransactionTypeEnum.SET_PRICE);
    requestTransaction.setQuantity(0);
    requestTransaction.setUnitPrice(BigDecimal.valueOf(-1.00));
    Stock expectedStock = getStock();
    expectedStock.setProductId(1);
    expectedStock.setQuantity(100);
    expectedStock.setUnitPrice(BigDecimal.valueOf(100.00));
    
    when(stockRepository.findById(anyInt()))
      .thenReturn(Mono.just(expectedStock));
    
    StepVerifier.create(appService.createTransaction(1, requestTransaction))
      .expectError(ValidationException.class)
      .verify();
    
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  private static void assertStock(Stock entity, StockDto dto){
    assertEquals(entity.getProductId(), dto.getProductId());
    assertEquals(entity.getQuantity(), dto.getQuantity());
    assertEquals(entity.getLastTransactionId(), dto.getLastTransactionId());
    assertNotNull(dto.getLastTransactionDate());
  }
  
  private static void assertTransaction(StockTransaction entity, StockTransactionDto dto){
    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getProductId(), dto.getProductId());
    assertEquals(entity.getQuantity(), dto.getQuantity());
    assertEquals(entity.getTransactionType(), dto.getTransactionType().getValue());
    assertNotNull(dto.getTransactionDate());
  }
  
  
  
}
