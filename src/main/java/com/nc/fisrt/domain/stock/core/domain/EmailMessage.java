package com.nc.fisrt.domain.stock.core.domain;

public class EmailMessage {

    private Long id;
    private String toEmail;
    private String subject;
    private String body;
    private SendStatus status;

    public EmailMessage(Long id, String toEmail, String subject, String body, SendStatus status) {
		this.id = id;
		this.toEmail = toEmail;
		this.subject = subject;
		this.subject = body;
		this.status = status;
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