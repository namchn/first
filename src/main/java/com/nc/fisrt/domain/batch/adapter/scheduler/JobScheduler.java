package com.nc.fisrt.domain.batch.adapter.scheduler;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;




/**
 * ⏱️ 스케줄러 클래스: 주기적으로 배치 작업을 실행
 */

@Slf4j
@Component // 스프링 컴포넌트로 등록
//@EnableScheduling // 스케줄링 기능 활성화
@RequiredArgsConstructor
public class JobScheduler {

    private final JobLauncher jobLauncher;
    private final Job logJob;
    
    // 실행 유무 체크
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    /**
     * 매 분 0초마다 logJob 실행
     */
    @Scheduled(cron = "0 * * * * *")
    public void runLogJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()) // Job 중복 실행 방지를 위한 파라미터
                .toJobParameters();

        jobLauncher.run(logJob, params);
    }
    
    
    //@Scheduled(fixedRate = 600000) // 10분마다 실행
    @Scheduled(cron = "0 * * * * *")
    public void runJob() {  //throws Exception 
    	
    	if (!isRunning.compareAndSet(false, true)) {
            log.info("이전 작업이 아직 끝나지 않았습니다. 이번 사이클은 건너뜀");
            return;
        }
    	
    	
        //if (isRunning.compareAndSet(false, true)) {  // 중복 방지
    	
            try {
                jobLauncher.run(
                		logJob,
                        new JobParametersBuilder()
                                .addLong("time", System.currentTimeMillis())
                                .toJobParameters()
                );
            } catch (Exception e) {
                log.error("Job 실행 중 예외 발생", e);
            } finally {
                isRunning.set(false);
            }
        
    }
    
}