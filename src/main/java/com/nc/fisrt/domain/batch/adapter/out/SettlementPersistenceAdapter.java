package com.nc.fisrt.domain.batch.adapter.out;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nc.fisrt.domain.batch.adapter.entity.TimeEntity;
import com.nc.fisrt.domain.batch.core.domain.Settlement;
import com.nc.fisrt.domain.batch.core.port.out.SettlementPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SettlementPersistenceAdapter implements SettlementPort {
    //private final JpaSettlementRepository repository;

    @Override
    public void save(Settlement settlement) {
        // 도메인 엔티티를 JPA 엔티티로 변환 후 저장
        //repository.save(SettlementJpaEntity.fromDomain(settlement));
    }

	@Override
	public void saveAll(List<TimeEntity> logs) {
		// TODO Auto-generated method stub
		
	}
}