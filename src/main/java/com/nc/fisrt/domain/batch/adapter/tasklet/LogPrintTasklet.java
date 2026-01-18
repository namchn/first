package com.nc.fisrt.domain.batch.adapter.tasklet;

import java.time.LocalDateTime;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class LogPrintTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        //System.out.println("🕒 로그 출력: " + LocalDateTime.now());
        log.info("🕒 로그 출력: " + LocalDateTime.now());
        return RepeatStatus.FINISHED;
    }
}