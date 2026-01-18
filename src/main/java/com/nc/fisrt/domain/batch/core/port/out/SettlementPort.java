package com.nc.fisrt.domain.batch.core.port.out;

import java.util.List;

import com.nc.fisrt.domain.batch.adapter.entity.TimeEntity;
import com.nc.fisrt.domain.batch.core.domain.Settlement;

public interface SettlementPort {
    void save(Settlement settlement);
	void saveAll(List<TimeEntity> logs);
}