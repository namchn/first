package com.nc.fisrt.domain.batch.adapter.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


//@Slf4j
@Component // 스프링 컴포넌트로 등록
//@RequiredArgsConstructor
public class BulkJobScheduler {
    private final JobLauncher jobLauncher;
    private final Job bulkInsertJob;

    public BulkJobScheduler(
	    		JobLauncher jobLauncher,
	    		@Qualifier("bulkInsertJob") Job bulkInsertJob) {
		this.jobLauncher= jobLauncher;
		this.bulkInsertJob= bulkInsertJob;
	}


    
    //@Scheduled(cron = "0 0 2 * * *") // 매일 새벽 2시 실행
    @Async("lowPrioExecutor") // 다른 스케쥴링이 영향받게 하지 않도록
    @Scheduled(cron = "0 0/10 * * * *") // 10분 마다
    public void runBulkJob() throws Exception {
        jobLauncher.run(bulkInsertJob, new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters());
    }
}