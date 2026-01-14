package com.nc.fisrt.domain.stock.adapter.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nc.fisrt.domain.stock.core.port.in.SendPendingEmailsUseCase;
import com.nc.fisrt.domain.stock.core.port.out.EmailMessageRepositoryPort;
import com.nc.fisrt.domain.stock.core.port.out.MailSenderPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailSendScheduler {

    private final SendPendingEmailsUseCase sendPendingEmailsUseCase;
    
    private final EmailMessageRepositoryPort repo;
    private final MailSenderPort mailSender;

    @Scheduled(cron = "0/10 * * * * *")
    public void sendEmails() {
    	//sendPendingEmailsUseCase.sendPendingEmails();
    	repo.findPending().forEach(msg -> {
            mailSender.send(msg);
            msg.markAsSent();
            repo.save(msg);
        });
    	
    }
}