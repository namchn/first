package com.nc.fisrt.domain.stock.core.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nc.fisrt.domain.stock.core.domain.EmailMessage;
import com.nc.fisrt.domain.stock.core.port.in.SendPendingEmailsUseCase;
import com.nc.fisrt.domain.stock.core.port.out.EmailMessageRepositoryPort;
import com.nc.fisrt.domain.stock.core.port.out.MailSenderPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailSendService implements SendPendingEmailsUseCase {

    private final EmailMessageRepositoryPort repo;
    private final MailSenderPort mailSender;

    @Override
    public void sendPendingEmails() {
        List<EmailMessage> messages = repo.findPending();

        for (EmailMessage msg : messages) {
            try {
                mailSender.send(msg);
                msg.markAsSent();
            } catch (Exception e) {
                msg.markAsFailed();
            }
            repo.save(msg);
        }
    }
}