package com.nc.fisrt.domain.batch.core.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nc.fisrt.domain.batch.adapter.entity.TimeEntity;
import com.nc.fisrt.domain.batch.adapter.out.persistence.TimeAdapter;
import com.nc.fisrt.domain.batch.adapter.tasklet.TimeSaveTasklet;
import com.nc.fisrt.domain.batch.core.port.in.CreateTestDataUseCase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
//@Slf4j
//@RequiredArgsConstructor
public class TestDataService implements CreateTestDataUseCase {

    private final TimeAdapter  timeAdapter;
    
    public TestDataService(TimeAdapter timeAdapter) {
        this.timeAdapter = timeAdapter;
    }

    @Override
    @Transactional
    public void createDefaultTests(int count) {

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

    }
    
    // 실패시 재시도로직 (무한재시도/제한 모드)
    private void saveWithRetry(List<TimeEntity> logs) {
        boolean success = false;
        int retryCount = 0;

        while (!success) {
            try {
            	timeAdapter.saveAll(logs);
                success = true;
                //log.info("DB 저장 성공: {}건", logs.size());
            } catch (Exception e) {
                retryCount++;
                //log.error("DB 저장 실패 (재시도 {}회): {}", retryCount, e.getMessage());

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