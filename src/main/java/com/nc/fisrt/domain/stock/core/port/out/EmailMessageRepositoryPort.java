package com.nc.fisrt.domain.stock.core.port.out;

import java.util.List;

import com.nc.fisrt.domain.stock.core.domain.EmailMessage;

//[Outbound Port] 도메인이 외부(DB)에 기대하는 역할
public interface EmailMessageRepositoryPort {
	EmailMessage findById(long msgId);
    void save(EmailMessage message);
    void markAsSent(Long id);
    List<EmailMessage> findPending();
	List<EmailMessage> findPending(boolean testYn);
	long updateStatusToSending(String status);
	long updateStatus(String status, Long id);
}
