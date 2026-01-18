package com.nc.fisrt.domain.batch.core.port.in;

import java.util.List;

import com.nc.fisrt.domain.batch.adapter.entity.TimeEntity;

public interface CreateTestDataUseCase {

	void createDefaultTests(int count);

	// 리스트 단위로 대량 저장 (Bulk Insert)
	List<TimeEntity> createChunkData(int i);

}
