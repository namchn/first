package com.nc.fisrt.domain.extension.adapter.persistence;

import org.springframework.stereotype.Component;

import com.nc.fisrt.domain.extension.core.domain.Member;
import com.nc.fisrt.domain.extension.core.port.MemberSavePort;

import lombok.RequiredArgsConstructor;

/*
Persistence Adapter (Outbound)
*/
@Component
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements MemberSavePort {
	private final JpaMemberRepository jpaRepository; // 실제 Spring Data JPA

	@Override
	public void save(Member member) {
		// 도메인 모델을 DB 엔티티로 변환하여 저장
		MemberEntity entity = MemberEntity.from(member);
		jpaRepository.save(entity);
	}
}