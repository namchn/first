package com.nc.fisrt.domain.stock.core.port.out;

import java.util.Map;

import reactor.core.publisher.Mono;

//application/port/out/StockExchangePort.java (외부 API 호출용)
public interface StockExchangePort {
	String getType(); // 본인이 어떤 구현체인지 반환
	Mono<Map<String, Object>> fetchDailyStockData(String symbol, String apiKey);
	Mono<Map<String, Object>> fetchDailyStockDataTest(String symbol, String apiKey);
}