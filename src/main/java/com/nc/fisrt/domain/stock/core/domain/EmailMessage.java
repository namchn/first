package com.nc.fisrt.domain.stock.core.domain;

import java.time.LocalDateTime;

public class EmailMessage {

    private Long id;
    private String toEmail;
    private String subject;
    private String body;
    private SendStatus status;
    private LocalDateTime createdAt;

    public EmailMessage( String toEmail, String subject, String body, SendStatus status) {
		//this.id = id;
		this.toEmail = toEmail;
		this.subject = subject;
		this.body = body;
		this.status = status;
		this.createdAt = LocalDateTime.now();
	}

    public EmailMessage(Long id, String toEmail, String subject, String body, SendStatus status) {
		this.id = id;
		this.toEmail = toEmail;
		this.subject = subject;
		this.body = body;
		this.status = status;
		this.createdAt = LocalDateTime.now();
	}

	public static EmailMessage create(String toEmail, String subject, String body) {
        return new EmailMessage(toEmail, subject, body, SendStatus.PENDING);
    }
    
    public static EmailMessage test(String symbol) {
        return new EmailMessage(
            "likencw@naver.com",
            "[TEST] " + symbol,
            "테스트 메시지",
            SendStatus.TEST
        );
    }
    
    public void markAsSent() {
        this.status = SendStatus.SENT;
    }
    
    public void markAsFailed() {
        this.status = SendStatus.FAILED;
    }
    
	public boolean isPending() {
        return status == SendStatus.PENDING;
    }

	public Long getId() {
		return this.id;
	}
	public String getToEmail() {
		return this.toEmail;
	}
	public String getSubject() {
		return this.subject;
	}
	public String getBody() {
		return this.body;
	}
	public SendStatus getStatus() {
		return this.status;
	}
}