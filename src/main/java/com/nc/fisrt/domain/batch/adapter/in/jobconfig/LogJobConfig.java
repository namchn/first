package com.nc.fisrt.domain.batch.adapter.in.jobconfig;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.nc.fisrt.domain.batch.adapter.out.persistence.TimeAdapter;
import com.nc.fisrt.domain.batch.adapter.tasklet.LogPrintTasklet;
import com.nc.fisrt.domain.batch.adapter.tasklet.TimeSaveTasklet;
import com.nc.fisrt.domain.batch.core.port.in.CreateTestDataUseCase;

import lombok.RequiredArgsConstructor;


/*
 * 📦 배치 작업을 정의하는 설정 클래스 
 * - Job: 하나 이상의 Step으로 구성된 단위 작업 
 * - Step: 실제 작업(tasklet)을
 * 실행
 * 
 * 이 클래스는 Spring Batch의 Job과 Step을 정의하여 배치 작업을 구성하는 역할을 한다.
 * 
 * 주요 구성 요소: 
 * - Job: 여러 개의 Step을 순차적으로 실행 
 * - Step: 실제로 수행할 배치 작업 (여기서는 로그 출력 작업)
 */

//@EnableBatchProcessing
@RequiredArgsConstructor
@Configuration // Spring의 설정 클래스를 나타내는 어노테이션, Spring IoC 컨테이너에 Bean으로 등록됨
public class LogJobConfig {

	
	 /**
     * Job을 정의하는 메서드
     * - Job은 여러 Step을 포함하며, 배치 작업의 전체 흐름을 정의
     * 
     * @param jobRepository 배치 작업의 메타데이터와 상태를 관리하는 JobRepository
     * @param logStep 실행할 Step 정의 (로그 출력 작업)
     * @return 설정된 Job 객체
     */
	
	
	private final CreateTestDataUseCase createTestDataUseCase; // Core 호출
	
    @Bean
    public Job logJob(JobRepository jobRepository,
            @Qualifier("logStep") Step logStep,
            @Qualifier("saveTimeStep") Step saveTimeStep,
            @Qualifier("testDataStep") Step testDataStep) {	// JobBuilder를 사용해 Job을 생성
    	return new JobBuilder("logJob", jobRepository) // "logJob"이라는 이름으로 Job 정의
                .start(logStep) // logStep을 Job의 첫 번째 실행 단계로 설정
                //.next(saveTimeStep)  // 다음 스텝 실행 
                .next(testDataStep)  // 다음 스텝 실행 
                .build(); // Job을 빌드하여 반환
    }
	
    
    
    /**
     * Step을 정의하는 메서드
     * - Step은 실제 배치 작업을 실행하는 단위로, 여기서는 Tasklet을 사용하여 로그를 출력
     * 
     * @param jobRepository 배치 작업의 메타데이터를 저장하는 JobRepository
     * @param transactionManager 트랜잭션을 관리하는 PlatformTransactionManager
     * @return 설정된 Step 객체
     */
    
    
    /*
    @Bean
    public Step logStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    	// StepBuilder를 사용해 Step을 정의
    	 return new StepBuilder("logStep", jobRepository) // "logStep"이라는 이름으로 Step 정의
                 .tasklet((contribution, chunkContext) -> { 
                	 
                	 // Tasklet을 정의 (여기서는 로그 출력)
                	 
                     // 배치 작업 중 출력할 로그
                     System.out.println(">>> 로그 출력: " + java.time.LocalDateTime.now());
                     return RepeatStatus.FINISHED; // 작업 완료 상태 반환 (작업이 완료되었음을 알림)
                     
                     
                 }, transactionManager) // 트랜잭션 관리자를 제공 (배치 작업의 트랜잭션을 관리)
                 .build(); // Step을 빌드하여 반환
    }
    */
    
    @Bean
    public Step logStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("logStep", jobRepository)
                .tasklet(new LogPrintTasklet(), transactionManager)
                .build();
    }
    
    
    
    // 시간 저장 Step
    @Bean
    public Step saveTimeStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, TimeAdapter timeAdapter) {
        return new StepBuilder("saveTimeStep", jobRepository)
                .tasklet(new TimeSaveTasklet(timeAdapter), transactionManager)
                .build();
    }
    
    // 3. 배치 Step 설정 (Tasklet 방식)
    @Bean
    public Step testDataStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("testDataStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    // Core 유스케이스 호출 (10개 생성)
                    createTestDataUseCase.createDefaultTests(10);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

   
}