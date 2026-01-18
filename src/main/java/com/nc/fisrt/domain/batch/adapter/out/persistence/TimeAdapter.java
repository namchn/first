package com.nc.fisrt.domain.batch.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nc.fisrt.domain.batch.adapter.entity.TimeEntity;
import com.nc.fisrt.domain.batch.adapter.out.persistence.repository.TimeRepository;
import com.nc.fisrt.domain.batch.core.port.out.TimeRepositoryPort;

@Component
//@RequiredArgsConstructor
public class TimeAdapter implements TimeRepositoryPort{

	private final TimeRepository timeRepository;	// 실제 Spring Data JPA

	public TimeAdapter(TimeRepository timeRepository) {
		this.timeRepository = timeRepository;
	}

	/*
	@Override
    public void saveAll(List<TestData> dataList) {
        // JPA 엔티티 변환 후 한꺼번에 저장
        List<TestDataEntity> entities = dataList.stream()
                .map(TestDataEntity::fromDomain)
                .toList();
        repository.saveAll(entities);
    }
    */
	
	@Override
    public void saveAll2(List<TimeEntity> dataList) {
		timeRepository.saveAll(dataList);
    }
	
	@Override
	public void saveAll(List<TimeEntity> logs) {
		timeRepository.saveAll(logs);
	}
	
}
