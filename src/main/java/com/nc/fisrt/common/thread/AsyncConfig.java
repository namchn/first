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
        executor.setThreadNamePrefix("immediate-");
        executor.setThreadPriority(Thread.MAX_PRIORITY); // 우선순위 높임
        
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(500); // 메모리 보호를 위해 적절한 제한을 둠
        // 핵심 설정: 대기실이 꽉 차면 작업을 버리지 않고, 호출한 스레드에서 직접 실행함 //용도: 실시간 알림, 인증 메일 등 즉시성이 필요한 작업에 적합합니다.
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        
        executor.initialize();
        return executor;
    }

    // 3번 프로그램(배치)을 위한 후순위 실행기 (우선순위 낮음)
    @Bean(name = "lowPrioExecutor")
    public Executor lowPrioExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);

        // 2. 큐 용량 확대 (메모리가 허용하는 한 넉넉히)
        executor.setQueueCapacity(1000); 
        
        executor.setThreadNamePrefix("low-prio-batch-");
        executor.setThreadPriority(Thread.MIN_PRIORITY); // 우선순위 낮춤 (1)
        
        // 3. 핵심: 큐가 가득 차면 스케줄러 스레드가 직접 실행하여 유실 방지
        // 결과적으로 시스템 전체의 속도는 느려지지만, 메일 발송 작업이 버려지는 일은 절대 없습니다.
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        
        // 4. 종료 시 남은 작업 처리 대기
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        
        executor.initialize();
        return executor;
    }
}