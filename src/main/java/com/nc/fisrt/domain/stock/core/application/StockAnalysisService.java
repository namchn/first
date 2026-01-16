package com.nc.fisrt.domain.stock.core.application;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.nc.fisrt.domain.stock.core.domain.EmailMessage;
import com.nc.fisrt.domain.stock.core.domain.SendStatus;
import com.nc.fisrt.domain.stock.core.domain.StockData;
import com.nc.fisrt.domain.stock.core.port.in.GetStockReportUseCase;
import com.nc.fisrt.domain.stock.core.port.out.EmailMessageRepositoryPort;
import com.nc.fisrt.domain.stock.core.port.out.LogPersistencePort;
import com.nc.fisrt.domain.stock.core.port.out.StockExchangePort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

//application/service/StockAnalysisService.java
//@Slf4j
@Service
@RequiredArgsConstructor
public class StockAnalysisService implements GetStockReportUseCase {
	
	
	// 스프링이 빈 이름(alpha, mock)을 key로 하여 자동으로 Map을 만들어 주입함
    private final Map<String, StockExchangePort> StockExchangePortMap;
	
	//private final StockExchangePort stockExchangePort;
	private final LogPersistencePort logPersistencePort;
	//private final StockData stockData; //상태없는 도메인 모델이므로
	private final EmailMessageRepositoryPort emailRepoPort;

	@Override
	public Mono<String> getStockReport(String type,boolean testYn ,String symbol, String apiKey) {
		// String symbol="TQQQ";
		
		StockExchangePort stockExchangePort = StockExchangePortMap.get(type.toLowerCase()); // 호출자가 넘긴 타입으로 찾기
		if (stockExchangePort == null) {
            throw new IllegalArgumentException("stockExchangePort 지원하지 않는 구현체입니다.");
        }
		
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
					//log.info("response:"+response);
					
					/*
					return new EmailMessage(
							0L,
							"",
		                    "",
		                    response,
		                    SendStatus.PENDING);
					*/
					
					
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
					
					
				})
				.onErrorResume(e -> 
					Mono.just("예외 발생: " + e.getMessage())
					//Mono.just("주식 데이터 조회 중 오류가 발생했습니다.")
				); // 예외(네트워크 오류, 타임아웃 등)만 처리
	}
	
	@Override
    public Mono<EmailMessage> getStockReport2(String type,boolean testYn, String symbol, String apiKey) {


		StockExchangePort stockExchangePort = StockExchangePortMap.get(type.toLowerCase()); // 호출자가 넘긴 타입으로 찾기
		if (stockExchangePort == null) {
            throw new IllegalArgumentException("stockExchangePort 지원하지 않는 구현체입니다.");
        }
		
        if (testYn) {
            EmailMessage test = EmailMessage.test(symbol);
            emailRepoPort.save(test);
            return Mono.just(test);
        }

        return stockExchangePort.fetchDailyStockData(symbol, apiKey)
            .map(rawData -> createEmail(symbol, rawData))
            .doOnNext(emailRepoPort::save);
    }
	
	private EmailMessage createEmail(String symbol, Map<String, Object> rawData) {
        StockData stockData = new StockData(symbol, rawData);
        String report = stockData.processAnalysis(symbol, rawData);

        return EmailMessage.create(
            "likencw@naver.com",
            "[주식 알림] " + symbol,
            report
        );
    }
	

}