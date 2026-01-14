package com.nc.fisrt.domain.stock.core.port.out;

import java.util.List;
import com.nc.fisrt.domain.stock.core.domain.EmailMessage;

//[Outbound Port] 도메인이 외부(DB)에 기대하는 역할
public interface EmailMessageRepositoryPort {
    void save(EmailMessage message);
    List<EmailMessage> findPending();
    void markAsSent(Long id);
}
