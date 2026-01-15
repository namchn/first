package com.nc.fisrt.domain.stock.adapter.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nc.fisrt.domain.stock.core.port.in.GetStockReportUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockFetchScheduler {

    private final GetStockReportUseCase getStockReportUseCase;

    //@Scheduled(cron = "0 0 17 * * MON-FRI")
    @Scheduled(cron = "11 16 18 * * *", zone = "Asia/Seoul")  // 일요일 오전 9시 12분 13초
    //@Scheduled(cron = "0 0/10 * * * *", zone = "Asia/Seoul")  // 일요일 오전 9시 12분 13초
    public void fetchAndStore() {
    	log.info("fetchAndStore");
    	boolean testYn = false;
    	//boolean testYn = true;
    	getStockReportUseCase.getStockReport2(testYn, "TQQQ", "API_KEY")
               .subscribe();
    }
}