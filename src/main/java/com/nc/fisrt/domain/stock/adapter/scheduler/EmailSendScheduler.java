package com.nc.fisrt.domain.stock.adapter.scheduler;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;

import com.nc.fisrt.domain.stock.core.domain.EmailMessage;
import com.nc.fisrt.domain.stock.core.port.in.SendPendingEmailsUseCase;
import com.nc.fisrt.domain.stock.core.port.out.EmailMessageRepositoryPort;
import com.nc.fisrt.domain.stock.core.port.out.MailSenderPort;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableAsync
@Slf4j
@RequiredArgsConstructor
public class EmailSendScheduler {

	private final SendPendingEmailsUseCase sendPendingEmailsUseCase;

	private final EmailMessageRepositoryPort repo;
	private final MailSenderPort mailSender;

	
	@Scheduled(cron = "0/30 * * * * *")
	public void sendEmails3() {
		//boolean testYn = true;
		boolean testYn =false;
		// 1. 발송 대상 조회 (영속성 컨텍스트를 위해 트랜잭션 없이 조회하거나 필요시 분리)
		//List<EmailMessage> pendingMsgs = repo.findPending(testYn);
		//log.info("sendEmails");
		//pendingMsgs.forEach(sendPendingEmailsUseCase::processEachEmail);
		
		// 1. 먼저 상태를 'SENDING'으로 바꿔서 다른 스케줄러가 못 가져가게 차단 (정합성 핵심)
	    repo.updateStatusToSending(testYn);
		
	    // 2. 내가 방금 점유한 'SENDING' 데이터들만 가져옴
		repo.findSending().forEach(EmailMessage -> {
		    // 3. 비동기로 발송 던지기
			// 엔티티를 도메인으로 변환하여 전달
	        sendPendingEmailsUseCase.sendAsync(EmailMessage.getId(), EmailMessage);
	    });
		
	}
	
	//@Scheduled(cron = "0/20 * * * * *")
	public void sendEmails2() {
		boolean testYn = true;
		//boolean testYn =false;
		// 1. 발송 대상 조회 (영속성 컨텍스트를 위해 트랜잭션 없이 조회하거나 필요시 분리)
		//List<EmailMessage> pendingMsgs = repo.findPending(testYn);
		//log.info("sendEmails");
		//pendingMsgs.forEach(sendPendingEmailsUseCase::processEachEmail);
		
		// 1. 먼저 상태를 'SENDING'으로 바꿔서 다른 스케줄러가 못 가져가게 차단 (정합성 핵심)
	    repo.updateStatusToSending(testYn);
		
		repo.findPending(testYn).forEach(id -> {
			sendPendingEmailsUseCase.processEachEmail(id); // ID만 전달
	    });
		
	}


    
    //@Scheduled(cron = "0/10 * * * * *")
    public void sendEmails() {
    	
    	boolean testYn =true;
    	//boolean testYn =false;
    	
    	log.info("sendEmails");
    	//sendPendingEmailsUseCase.sendPendingEmails();
    	repo.findPending(testYn).forEach(msg -> {
            mailSender.send(msg);
            msg.markAsSent();
            updatemsg(msg);
            //repo.save(msg);
        });
    	
    }
    
    @Transactional
    public void updatemsg(EmailMessage msg ){
    	repo.save(msg);
    }
}