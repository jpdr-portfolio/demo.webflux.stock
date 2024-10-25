package com.jpdr.apps.demo.webflux.stock.configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.Http11SslContextSpec;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {
  
  @Value("${app.base-url.product}")
  private String productBaseUrl;
  
  @Bean(name = "productWebClient")
  public WebClient userWebClient(WebClient.Builder webClientBuilder){
    ConnectionProvider connectionProvider = ConnectionProvider.builder("connectionProvider")
      .maxIdleTime(Duration.ofSeconds(10))
      .build();
    Http11SslContextSpec sslContextSpec = Http11SslContextSpec.forClient();
    return webClientBuilder.baseUrl(productBaseUrl)
      .clientConnector(
        new ReactorClientHttpConnector(HttpClient.create(connectionProvider)
          .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
          .responseTimeout(Duration.ofMillis(5000))
          .doOnConnected( connection ->  connection
            .addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
            .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)))
          .secure(spec -> spec.sslContext(sslContextSpec)
            .handshakeTimeout(Duration.ofMillis(5000))
            .closeNotifyFlushTimeout(Duration.ofMillis(5000))
            .closeNotifyReadTimeout(Duration.ofMillis(5000)))
      ))
      .exchangeStrategies(ExchangeStrategies.builder()
        .codecs(codecConfig -> codecConfig.defaultCodecs().maxInMemorySize(500 * 1024))
        .build())
      .build();
  }
  
}
