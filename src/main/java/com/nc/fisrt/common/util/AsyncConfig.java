package com.nc.fisrt.common.util;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    // 1 & 2번 프로그램을 위한 일반 실행기 (우선순위 높음)
    @Bean(name = "immediateExecutor")
    public Executor immediateExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setThreadNamePrefix("immediate-");
        executor.setThreadPriority(Thread.MAX_PRIORITY); // 우선순위 높임
        executor.initialize();
        return executor;
    }

    // 3번 프로그램(배치)을 위한 후순위 실행기 (우선순위 낮음)
    @Bean(name = "lowPrioExecutor")
    public Executor lowPrioExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setThreadNamePrefix("low-prio-batch-");
        executor.setThreadPriority(Thread.MIN_PRIORITY); // 우선순위 낮춤 (1)
        executor.initialize();
        return executor;
    }
}