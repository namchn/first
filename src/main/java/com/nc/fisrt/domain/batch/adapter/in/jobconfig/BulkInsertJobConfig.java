package com.nc.fisrt.domain.batch.adapter.in.jobconfig;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StopWatch;

import com.nc.fisrt.domain.batch.adapter.entity.TimeEntity;
import com.nc.fisrt.domain.batch.core.port.in.CreateTestDataUseCase;
import com.nc.fisrt.domain.batch.core.port.out.TimeRepositoryPort;

import lombok.extern.slf4j.Slf4j;


@Slf4j
//@RequiredArgsConstructor
@Configuration
public class BulkInsertJobConfig {

	private final CreateTestDataUseCase createTestDataUseCase; // Core 호출
	private final TimeRepositoryPort timeRepositoryPort;
	
	public BulkInsertJobConfig(
			CreateTestDataUseCase createTestDataUseCase,
			TimeRepositoryPort timeRepositoryPort) {
		this.createTestDataUseCase= createTestDataUseCase;
		this.timeRepositoryPort= timeRepositoryPort;
	}


    @Bean(name = "bulkInsertJob")
    //@Primary
    //@Qualifier("bulkInsertJob")
    public Job bulkInsertJob(JobRepository jobRepository
    						,@Qualifier("bulkInsertStep") Step bulkInsertStep
    						//,Step bulkInsertStep
    						) {
        return new JobBuilder("bulkInsertJob", jobRepository)
                .start(bulkInsertStep)
                
                // 시간 측정
                .listener(new JobExecutionListener() {
                    StopWatch stopWatch = new StopWatch();

                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        stopWatch.start();
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        stopWatch.stop();
                        log.info("▶▶▶ 총 소요 시간: {}초", stopWatch.getTotalTimeSeconds());
                    }
                })
                //
                
                .build();
    }

    @Bean
    public Step bulkInsertStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("bulkInsertStep", jobRepository)
                .<List<TimeEntity>, List<TimeEntity>>chunk(100, transactionManager) // 100개 단위 트랜잭션
                .reader(new ItemReader<>() {
                    private int count = 0;
                    private final int max = 1000; // 총 10만 개를 넣고 싶다면 여기서 조절

                    @Override
                    public List<TimeEntity> read() {
                        if (count >= max) return null; // 종료 조건
                        count++;
                        return createTestDataUseCase.createChunkData(100); // 100개씩 생성
                    }
                })
                .writer(chunk -> {
                    // Outbound Port를 통해 대량 저장
                    chunk.getItems().forEach(timeRepositoryPort::saveAll2);
                    log.info("Batch Inserted {} rows...", chunk.getItems().size() * 100);
                })
                .build();
    }
}
