package com.nc.fisrt.domain.stock.adapter.scheduler;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nc.fisrt.common.service.EmailService;
import com.nc.fisrt.domain.stock.core.application.StockAnalysisService;
import com.nc.fisrt.domain.stock.core.port.in.GetStockReportUseCase;

import lombok.extern.slf4j.Slf4j;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Configuration
@EnableScheduling
public class ApiSchedulerServiceImpl {


	private  ObjectMapper mapper = new ObjectMapper();
    //private final KiwoomService kiwoomService;
    //private final KakaoAlarmService kakaoAlarmService;
    //private final KiwoomLogicContainerService kiwoomLogicContainerService;
    private final EmailService emailService;
    //private final YahoofinaceStockService yahoofinaceStockService;
    
    private final StockAnalysisService stockAnalysisService;
    private final GetStockReportUseCase getStockReportUseCase;
    
    
    
    //private final KakaoTokenManager kakaoTokenManager;
    
    /*
	public ApiSchedulerServiceImpl(
			KiwoomService kiwoomService
			,KakaoAlarmService kakaoAlarmService
			//,KakaoTokenManager kakaoTokenManager
			,KiwoomLogicContainerService kiwoomLogicContainerService
			,EmailService emailService
			,YahoofinaceStockService yahoofinaceStockService
			,StockAnalysisService stockAnalysisService
			) {
		this.kiwoomService = kiwoomService;
		this.kakaoAlarmService = kakaoAlarmService;
		//this.kakaoTokenManager = kakaoTokenManager;
		this.kiwoomLogicContainerService = kiwoomLogicContainerService;
		this.emailService = emailService;
		this.yahoofinaceStockService = yahoofinaceStockService;
		this.stockAnalysisService = stockAnalysisService;
	}
	*/
	

	public ApiSchedulerServiceImpl(
			EmailService emailService
			,StockAnalysisService stockAnalysisService
			,GetStockReportUseCase getStockReportUseCase
			) {
		this.emailService = emailService;
		this.stockAnalysisService = stockAnalysisService;
		this.getStockReportUseCase = getStockReportUseCase;
	}
	
	//@Scheduled(fixedRate = 120000) // 2분마다
	//@Scheduled(cron = "00 08 09 * * MON")  // 월요일 오후 2시 12분 13초
	//@Scheduled(cron = "0 0/2 * * * MON-FRI", zone = "Asia/Seoul")
	
	//@Scheduled(cron = "0 24 18 * * MON-FRI", zone = "Asia/Seoul")  // 2의 배수 분 마다
	
	//@Scheduled(cron = "10 48 22 * * MON-FRI", zone = "Asia/Seoul")  // 2의 배수 분 마다
	@Scheduled(cron = "11 36 00 * * *", zone = "Asia/Seoul")  // 일요일 오전 9시 12분 13초
	public void runSequentialYahoofinaceTqqq() {
		
		boolean testYn =true;
		//boolean testYn =false;
		//Map<String, Object> body = yahoofinaceStockService.getStockQuote("TQQQ","undefined");
    	//Map<String, Object> body = yahoofinaceStockService.getStockQuote2("TQQQ","undefined");
    	//JsonNode bodyNode = mapper.valueToTree(body);
    	//log.info("bodyNode:"+bodyNode.toPrettyString());
    	
    	
    	/*
    	JsonNode Quote = bodyNode.get("Global Quote");
    	log.info("Global Quote:"+Quote.toPrettyString());
    	JsonNode price = Quote.get("05. price");
    	JsonNode trading_day = Quote.get("07. latest trading day");
    	log.info("price:"+price.asText());
    	log.info("trading_day:"+trading_day.asText());
    	*/
    	
    	
    	/*
    	String response = yahoofinaceStockService.compareCloseWithSMA200("TQQQ","undefined");
    	log.info("response:"+response);
    	if(!response.isEmpty()) {
        	try {
                emailService.sendEmail(
                        "likencw@naver.com",  //보낼주소
                        "test.nc2030 에서 보내는 부정기메일입니다.",         //제목
                        "이건 스프링 부트에서 보내는 테스트 메일입니다.\n"+response //내용
                );
                log.info("메일 발송 완료 ✅")
            } catch (Exception e) {
                e.printStackTrace();
            }
    	}
    	*/
    	
    	
    	//호출은 비동기 -> 메일은 동기
    	//yahoofinaceStockService.getStockReport(testYn,"TQQQ","undefined")
    	//stockAnalysisService.getStockReport(testYn,"TQQQ","undefined")
    	getStockReportUseCase.getStockReport(testYn,"TQQQ","undefined")
    	
    	//yahoofinaceStockService.getStockReport(testYn,"FNGG","undefined")
    							//.block()
    							//.filter(response -> response != null) // null 값은 downstream으로 안 보냄
						    	.publishOn(Schedulers.boundedElastic()) // 블로킹 코드 안전하게 실행→ 블로킹 메일 전송을 별도 스레드 풀에서 실행
						        .doOnNext(response -> 
								        {
									        //if(response.length()>0) {//종가가 아래에 있을때만 확인
									        	log.info("response:"+response);
										        emailService.sendEmail(
										        		"likencw@naver.com",  //보낼주소
								                        "[알림]test.nc2030 에서 보내는 부정기메일입니다.",         //제목
								                        "이건 스프링 부트에서 보내는 테스트 메일입니다.\n"+response //내용
										        );
									        //}
								        }
						        )
						        .subscribe(
						                success -> log.info("메일 발송 프로세스 완료 ✅"),
						                error -> log.info("메일 발송 실패 ❌ " + error.getMessage())
						        );
    }
	
	
    
}
