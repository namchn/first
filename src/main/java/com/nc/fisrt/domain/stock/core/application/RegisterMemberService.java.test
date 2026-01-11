package com.nc.fisrt.domain.extension.core.application;

import org.springframework.stereotype.Service;

import com.nc.fisrt.domain.extension.core.domain.Member;
import com.nc.fisrt.domain.extension.core.port.MemberSavePort;
import com.nc.fisrt.domain.extension.core.port.RegisterCommand;
import com.nc.fisrt.domain.extension.core.port.RegisterMemberUseCase;

import lombok.RequiredArgsConstructor;

//[Service] 비즈니스 흐름을 조율 (포트를 사용함)
@Service
@RequiredArgsConstructor
public class RegisterMemberService implements RegisterMemberUseCase {
	private final MemberSavePort memberSavePort; // 인터페이스에 의존

	@Override
	public void register(RegisterCommand command) {
		Member member = new Member(command.getEmail(), command.getName());
		// ... 중복 체크 등 비즈니스 로직 ...
		memberSavePort.save(member); // 실제 저장은 누가 하는지 모름
	}

}