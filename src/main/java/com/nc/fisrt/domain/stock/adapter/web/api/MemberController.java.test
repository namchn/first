package com.nc.fisrt.domain.extension.web.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nc.fisrt.domain.extension.core.port.RegisterMemberUseCase;

import lombok.RequiredArgsConstructor;

/*Infrastructure: Adapters (바깥쪽, 실제 기술 구현)
이제 도메인이 선언한 인터페이스를 실제 기술(JPA, RestTemplate 등)로 구현합니다.
Web Adapter (Inbound)
*/
@RestController
@RequiredArgsConstructor
public class MemberController {
	private final RegisterMemberUseCase registerMemberUseCase; // 인터페이스에 의존

	@PostMapping("/members")
	public void register(@RequestBody MemberRequest request) {
		// 컨트롤러는 유즈케이스(포트)만 호출
		registerMemberUseCase.register(new RegisterCommand(request.email, request.name));
	}
}