package com.nc.fisrt.domain.stock.adapter.out.persistence;

import org.springframework.stereotype.Component;

import com.nc.fisrt.domain.stock.core.port.out.LogPersistencePort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//adapter/out/persistence/KiwoomLogAdapter.java

@Slf4j
@Component
@RequiredArgsConstructor
public class KiwoomLogAdapter  implements LogPersistencePort 
{
	/*
	private final KiwoomApiLogService kiwoomApiLogService; // 기존 서비스 재사용 가능

	private static final String DAILY_URL =
            "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol={symbol}&apikey={apikey}&outputsize=full";

	
	@Override
	public void recordApiLog(String symbol, double stopWatchGetTotalTimeSeconds) {
		String content = new StringBuilder()
                .append("getStockReport")
                .append("&symbol:").append(String.valueOf(symbol))
                .append("&runningTime:").append(stopWatchGetTotalTimeSeconds).append(" s")
                .toString();
		kiwoomApiLogService.increaseLogAsync(DAILY_URL, content);
	}
	*/
	
	@Override
	public void recordApiLog(String symbol, double stopWatchGetTotalTimeSeconds) {
		String content = new StringBuilder()
                .append("getStockReport")
                .append("&symbol:").append(String.valueOf(symbol))
                .append("&runningTime:").append(stopWatchGetTotalTimeSeconds).append(" s")
                .toString();
		log.debug("content: "+content);	
		//kiwoomApiLogService.increaseLogAsync(DAILY_URL, content);
	}
}