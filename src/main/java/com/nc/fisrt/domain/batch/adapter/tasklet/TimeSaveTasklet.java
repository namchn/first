package com.nc.fisrt.domain.batch.adapter.tasklet;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.nc.fisrt.domain.batch.adapter.entity.TimeEntity;
import com.nc.fisrt.domain.batch.adapter.out.persistence.TimeAdapter;
import com.nc.fisrt.domain.batch.adapter.out.persistence.repository.TimeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeSaveTasklet implements Tasklet {

    /*
    TimeSaveTasklet 생성자에 infiniteRetry / maxRetry 값을 줘서 모드 선택 가능
    무한 재시도 모드 → 끝날 때까지 계속 시도 (다음 스케줄 오더라도 이전 Job이 끝날 때까지 대기)
    제한 모드 → 실패한 chunk는 로그 남기고 skip 후 다음 작업으로 진행
    */
    //private final boolean infiniteRetry; // 무한 재시도 모드 여부
    //private final int maxRetry;          // 최대 재시도 횟수 (무한 모드 아닐 때 사용)
    //private final int batchSize;         // chunk 단위 insert 크기
    //private final int dataSize;          // 생성할 데이터 개수
    

    //private final TimeRepository timeRepository;
    
    
    private final TimeAdapter  timeAdapter;
    
    public TimeSaveTasklet(TimeAdapter timeAdapter) {
        this.timeAdapter = timeAdapter;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
    	 //log.info("TimeSaveTasklet 실행 시작: {}개 데이터 생성", dataSize);

    	
    	// 5000개 생성
        List<TimeEntity> allLogs = new ArrayList<>();
        int dataSize = 20; //20
        for (int i = 0; i < dataSize; i++) {
            allLogs.add(new TimeEntity(LocalDateTime.now().plusNanos(i * 1000)));
        }

        // 100개씩 잘라서 insert
        int batchSize = 10; //10
        for (int i = 0; i < allLogs.size(); i += batchSize) {
            List<TimeEntity> subList = allLogs.subList(i, Math.min(i + batchSize, allLogs.size()));
            saveWithRetry(subList);
        }

        /*
        // 5000개 생성 (나노초 단위 차이를 줘서 중복 방지)
        List<TimeEntity> logs = IntStream.range(0, 10)
                .mapToObj(i -> new TimeEntity(  LocalDateTime.now().plusNanos(i * 1000)   ))
                //  baseTime.plusNanos(i * 1000)
                .collect(Collectors.toList());
        
        timeRepository.saveAll(logs);
    	*/
        
        //1.
    	//timeRepository.save(TimeEntity.ofNow());
        
        
        
        
        //timeRepository.save(new TimeEntity(LocalDateTime.now()));
        log.info("TimeSaveTasklet 실행 완료");
        return RepeatStatus.FINISHED;
    }
    
    // 실패시 재시도로직 (무한재시도/제한 모드)
    private void saveWithRetry(List<TimeEntity> logs) {
        boolean success = false;
        int retryCount = 0;

        while (!success) {
            try {
            	timeAdapter.saveAll(logs);
                success = true;
                log.info("DB 저장 성공: {}건", logs.size());
            } catch (Exception e) {
                retryCount++;
                log.error("DB 저장 실패 (재시도 {}회): {}", retryCount, e.getMessage());

                /*
                // 2) 다음 스케줄 전까지 재시도 모드
                if (retryUntilNextSchedule) {
                    if (System.currentTimeMillis() >= endTime) {
                        log.error("스케줄 제한 시간 초과 → 해당 chunk 저장 실패, skip: {}건", logs.size());
                        break;
                    }
                    sleep(2000);
                    continue;
                }
                */
                
                /* 
                // 3) 최대 재시도 모드
                if (!infiniteRetry && retryCount >= maxRetry) {
                    System.err.println("최대 재시도 횟수 도달 → 해당 chunk는 저장 실패로 종료");
                    break; // 실패한 데이터는 로그만 남기고 넘어감
                }
                */
                
                try {
                    Thread.sleep(2000); // 2초 대기 후 재시도
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    
}