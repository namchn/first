package com.nc.fisrt.domain.stock.adapter.out.external;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.nc.fisrt.domain.stock.core.port.out.StockExchangePort;

import reactor.core.publisher.Mono;

//adapter/out/external/AlphaVantageAdapter.java
@Component
public class AlphaVantageAdapter implements StockExchangePort {
	private final WebClient webClient;

	public AlphaVantageAdapter(WebClient.Builder webClientBuilder) {
		this.webClient = 
				webClientBuilder.baseUrl("https://www.alphavantage.co")
				.codecs(configurer -> configurer
		        .defaultCodecs()
		        .maxInMemorySize(16 * 1024 * 1024)) // 16MB로 버퍼 확대
				.build();
	}

	@Override
	public Mono<Map<String, Object>> fetchDailyStockData(String symbol, String apiKey) {
		return webClient.get()
					.uri(uriBuilder -> uriBuilder.path("/query")
                    .queryParam("function", "TIME_SERIES_DAILY")
                    .queryParam("symbol", symbol)
                    .queryParam("apikey", apiKey)
                    .build())
				.retrieve()
				// ParameterizedTypeReference를 사용하여 명시적으로 타입을 지정
				.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
				// .bodyToMono(Map.class); // API 응답(JSON)을 Map으로 받음
				//.map(map -> (Map<String, Object>) map); // 강제 캐스팅
	}
}