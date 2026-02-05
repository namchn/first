package com.nc.fisrt.common.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    // 1 & 2번 프로그램을 위한 일반 실행기 (우선순위 높음)
    @Bean(name = "immediateExecutor")
    @Primary
    public Executor immediateExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(50);
        
        executor.setThreadNamePrefix("immediate-");
        executor.setThreadPriority(Thread.MAX_PRIORITY); // 우선순위 높임
        
        executor.setQueueCapacity(500); // 메모리 보호를 위해 적절한 제한을 둠
        
        // 핵심 설정: 대기실이 꽉 차면 작업을 버리지 않고, 호출한 스레드에서 직접 실행함 
        // 용도: 실시간 알림, 인증 메일 등 즉시성이 필요한 작업에 적합합니다.
        // CallerRunsPolicy는 시스템 전체를 느리게 할 수 있음
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        
        // 선착순이라면 차라리 에러를 내고 Redis 대기열로 보내는 설계가 필요
        // executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy()); 
        
        executor.initialize();
        return executor;
    }

    // 3번 프로그램(배치)을 위한 후순위 실행기 (우선순위 낮음)
    @Bean(name = "lowPrioExecutor")
    public Executor lowPrioExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
     
        // 1. 최소한의 자원만 점유 (평상시)
        executor.setCorePoolSize(4); 
     
        // 2. 자원이 충분하고 큐가 차면 최대 이만큼까지 쓰레드를 늘림 (고부하 시)
        // 노트북의 논리 프로세서 수(예: 16) 근처로 설정
        executor.setMaxPoolSize(16); 

        // 2. 큐 용량 확대 (메모리가 허용하는 한 넉넉히)
        // 3. 큐를 너무 크게 잡으면 maxPoolSize까지 쓰레드가 늘어나지 않음
        // 큐가 꽉 차야 쓰레드가 증가하므로, 적당한 수치(예: 500)로 설정
        executor.setQueueCapacity(1000); 
        
        // 1. 큐를 충분히 크게 잡음 (메모리 16GB 노트북 기준 10만 건 정도는 수용 가능)
        //executor.setQueueCapacity(100000); 
        
        executor.setThreadNamePrefix("low-prio-batch-");
        executor.setThreadPriority(Thread.MIN_PRIORITY); // 우선순위 낮춤 (1)
        
        // 3. 핵심: 큐가 가득 차면 스케줄러 스레드가 직접 실행하여 유실 방지
        // 5. 부하 조절 장치: 쓰레드와 큐가 모두 꽉 찼을 때만 호출자(스케줄러)가 직접 실행
        // 이로 인해 스케줄러의 다음 '조회' 속도가 자연스럽게 늦춰지며 시스템 부하를 제어(Backpressure)합니다.
                
        // 결과적으로 시스템 전체의 속도는 느려지지만, 메일 발송 작업이 버려지는 일은 절대 없습니다.
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        
        // 2. 핵심 변경: CallerRuns 대신 '대기' 혹은 '기록 후 재시도' 전략 필요
        // 일단 시스템 멈춤 방지를 위해 AbortPolicy를 쓰고, 예외 발생 시 DB에 '미발송' 상태로 저장
        //executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        
        // 4. 종료 시 남은 작업 처리 대기
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //executor.setAwaitTerminationSeconds(60);
        executor.setAwaitTerminationSeconds(600); // 종료 시 충분한 시간 부여
        
        executor.initialize();
        return executor;
    }
}