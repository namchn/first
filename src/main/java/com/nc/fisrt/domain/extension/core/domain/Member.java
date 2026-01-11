package com.nc.fisrt.domain.extension.core.domain;

//[Domain Entity] 비즈니스 규칙의 중심 외부 라이브러리 의존성 없이 순수한 Java 객체로 만듭니다.
public class Member {
	private Long id;
	private String email;
	private String name;

	// 전체 필드 생성자 (내부용)
    public Member(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
	

	// 서비스 계층에서 "DTO"처럼 생성할 때 사용하는 정적 메서드
    public static Member createForRegistration(String email, String name) {
        return new Member(null, email, name);
    }
	
    // 도메인 비즈니스 로직
    public void validateRegistration() {
        if (!email.contains("@")) throw new IllegalArgumentException("이메일 형식이 잘못되었습니다.");
    }
    
	// 비즈니스 로직: "회원 이름은 비어있을 수 없다"
	public void updateName(String newName) {
		if (newName == null || newName.isBlank())
			throw new IllegalArgumentException();
		this.name = newName;
	}

	// Constructor, Getter 생략
}