package com.nc.fisrt.domain.extension.core.port;

import com.nc.fisrt.domain.extension.core.domain.Member;

//[Outbound Port] 도메인이 외부(DB)에 기대하는 역할
public interface MemberSavePort {
	void save(Member member);
}