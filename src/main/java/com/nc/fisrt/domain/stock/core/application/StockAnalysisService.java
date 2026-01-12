package com.nc.fisrt.domain.stock.core.application;

import org.springframework.stereotype.Service;

import com.nc.fisrt.domain.stock.core.domain.StockData;
import com.nc.fisrt.domain.stock.core.port.in.GetStockReportUseCase;
import com.nc.fisrt.domain.stock.core.port.out.LogPersistencePort;
import com.nc.fisrt.domain.stock.core.port.out.StockExchangePort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

//application/service/StockAnalysisService.java
@Slf4j
@Service
@RequiredArgsConstructor
public class StockAnalysisService implements GetStockReportUseCase {
	private final StockExchangePort stockExchangePort;
	private final LogPersistencePort logPersistencePort;
	//private final StockData stockData; //상태없는 도메인 모델이므로
	
	

	@Override
	public Mono<String> getStockReport(boolean testYn ,String symbol, String apiKey) {
		// String symbol="TQQQ";
		
		 boolean mockMode = testYn;
		 if (mockMode) {
	         return Mono.just("테스트 응답: " + symbol + ", " + apiKey);
	     }

		
		// StopWatch 대신 시스템 시간을 사용하여 프레임워크 의존성 최소화 가능
		//StopWatch stopWatch = new StopWatch();
		//stopWatch.start();
        long startTime = System.currentTimeMillis();
		
		return stockExchangePort.fetchDailyStockData(symbol, apiKey)
				.map(rawData -> {
					
					//어댑터에서 받은 Raw 데이터를 도메인 모델로 변환 (도메인 내부에서 계산)
					StockData stockData = new StockData(symbol, rawData);
					String response = stockData.processAnalysis(symbol, rawData);
					log.info("response:"+response);
					return response;
					
                    //StockData stockData = StockDataParser.parse(symbol, rawData);
                    //return stockData.generateReport();
					
					}) 
				// 계산 로직																			
				// 분리
				//데이터 응답후 처리
				.doOnTerminate(() -> {
					// 정상/에러 종료 시 모두 실행 //db 저장
					//stopWatch.stop();
					//logPersistencePort.recordApiLog(symbol, stopWatch.getTotalTimeSeconds());
					
					// 로그 기록은 비즈니스 성공 시 수행
                    double duration = (System.currentTimeMillis() - startTime) / 1000.0;
					logPersistencePort.recordApiLog(symbol, duration);
					
				}).onErrorResume(e -> Mono.just("예외 발생: " + e.getMessage())); // 예외(네트워크 오류, 타임아웃 등)만 처리
	}

	
}