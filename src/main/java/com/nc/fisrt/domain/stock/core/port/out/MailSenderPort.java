package com.nc.fisrt.domain.stock.core.port.out;

import com.nc.fisrt.domain.stock.core.domain.EmailMessage;

public interface MailSenderPort {
    void send(EmailMessage message);
	void sendEmail(String to, String subject, String text);
}
