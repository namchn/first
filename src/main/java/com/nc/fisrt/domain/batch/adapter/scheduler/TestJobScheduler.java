package com.nc.fisrt.domain.batch.adapter.scheduler;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component // 스프링 컴포넌트로 등록
//@RequiredArgsConstructor
public class TestJobScheduler {
    
	
	
    private final MeterRegistry meterRegistry;
    //private final MyMemoryLogRepository repository; // DB 저장을 위한 Repository
    TestJobScheduler (MeterRegistry meterRegistry
    		//,MyMemoryLogRepository repository
    		){
    	this.meterRegistry = meterRegistry;
    	//this.repository = repository;
    }
    
    
    
    @Async  //스케줄러 스레드는 배치를 별도의 '작업 스레드(Task Executor)'에 던져버리고 스케줄러 풀로 복귀
    @Scheduled(cron = "0 0/6 * * * *") // 6분 마다
    public void runTestJob() throws Exception {
    	// 1. 데이터 추출 (단위: Byte)
        double usedMemory = meterRegistry.get("jvm.memory.used")
                            .tag("area", "heap").gauge().value();
        double maxMemory = meterRegistry.get("jvm.memory.max")
                            .tag("area", "heap").gauge().value();
        
        log.info(Double.toString(usedMemory/1024/1024));
        log.info(Double.toString(maxMemory/1024/1024));
        
    }
    
    /*
    */
	
	
	
	
    
	/*
    Runtime runtime = Runtime.getRuntime();
    long maxMemory = runtime.maxMemory();      // 최대 설정 용량
    long totalMemory = runtime.totalMemory();  // 현재 할당된 용량
    long freeMemory = runtime.freeMemory();    // 할당된 용량 중 남은 여유분
    long usedMemory = totalMemory - freeMemory; // 실제 사용 중인 양

    
    
    @Async  //스케줄러 스레드는 배치를 별도의 '작업 스레드(Task Executor)'에 던져버리고 스케줄러 풀로 복귀
    @Scheduled(cron = "0 0/6 * * * *") // 6분 마다
    public void runTestJob() throws Exception {

        log.info(Long.toString(maxMemory/1024/1024  ));
        log.info(Long.toString(totalMemory/1024/1024  ));
        log.info(Long.toString(freeMemory/1024/1024  ));
        log.info(Long.toString(usedMemory/1024/1024  ));
        
    }
    
    */
    
}