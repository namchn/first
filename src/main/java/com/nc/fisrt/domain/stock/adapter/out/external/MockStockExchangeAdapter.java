package com.nc.fisrt.domain.stock.adapter.out.external;

import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.nc.fisrt.domain.stock.core.port.out.StockExchangePort;

import reactor.core.publisher.Mono;

//@Component
@Component("mock")
//@Profile("test")
public class MockStockExchangeAdapter implements StockExchangePort {

	

	@Override
	public String getType() {
		return "mock";
	}
	
    @Override
    public Mono<Map<String, Object>> fetchDailyStockData(String symbol, String apiKey) {
        Map<String, Object> mock = Map.of(
            "Time Series (Daily)", Map.of(
                "2024-01-01", Map.of("4. close", "111.22")
            ),
            "Meta Data", Map.of("2. Symbol", symbol)
        );

        return Mono.just(mock);
    }

	@Override
	public Mono<Map<String, Object>> fetchDailyStockDataTest(String symbol, String apiKey) {
		// TODO Auto-generated method stub
		return null;
	}
}
