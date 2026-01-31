package com.nc.fisrt.domain.user.adapter.web.view;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {

	
	//final private VisitCounterService visitCounterService;
	
	@GetMapping("/index")
	public String healthCheck() {
		//test//
		//int visitCount = visitCounterService.todayUriCount("/index");
		LocalDate today = LocalDate.now();
		//return "The service is up and running... by ncw"  + "<br>오늘(미국서부기준:" +today+ ") 접속횟수 : " + visitCount ;
		return "The service is up and running... by ncw"  + "<br>오늘(서버시간기준:" +today+ ")  " ;
	}
	
	
	/*
	@GetMapping("/")
	public ResponseEntity<String> healthCheckRedirect() {
		// 리다이렉트 URL을 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/home");  // 리다이렉트 경로 설정

        // 3xx 리다이렉션 상태 코드 반환
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
	}
	 */

	/*
	 * @GetMapping("/robots.txt") public String robotsTxt() { return
	 * "User-agent: *\n   Disallow: /md$ \n     Allow: /md \n"; }
	 */

}
