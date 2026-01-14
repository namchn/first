package com.nc.fisrt.domain.stock.core.port.in;

import com.nc.fisrt.domain.stock.core.domain.EmailMessage;

public interface SendPendingEmailsUseCase {
	void sendPendingEmails();
	void processEachEmail(EmailMessage msg);
	void processEachEmail(long id);
}
