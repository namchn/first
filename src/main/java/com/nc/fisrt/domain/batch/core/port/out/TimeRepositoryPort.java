package com.nc.fisrt.domain.batch.core.port.out;

import java.util.List;

import com.nc.fisrt.domain.batch.adapter.entity.TimeEntity;

public interface TimeRepositoryPort {

	void saveAll(List<TimeEntity> logs);

}
