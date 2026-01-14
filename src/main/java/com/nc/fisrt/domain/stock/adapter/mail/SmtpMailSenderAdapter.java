package com.nc.fisrt.domain.stock.adapter.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.nc.fisrt.domain.stock.core.domain.EmailMessage;
import com.nc.fisrt.domain.stock.core.port.out.MailSenderPort;

public class SmtpMailSenderAdapter implements MailSenderPort {

	private final JavaMailSender mailSender;

	// @Value("${spring.mail.username}")
	private String fromAddress;

	SmtpMailSenderAdapter(@Value("${spring.mail.username}") String fromAddress, JavaMailSender mailSender) {
		this.fromAddress = fromAddress;
		this.mailSender = mailSender;
	}

	@Override
	public void sendEmail(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromAddress);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);

		mailSender.send(message);
	}

	@Override
	public void send(EmailMessage email) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromAddress);
		message.setTo(email.getToEmail());
		message.setSubject(email.getSubject());
		message.setText(email.getBody());

		mailSender.send(message);
	}

}