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

	@Scheduled(cron = "0/20 * * * * *")
	public void sendEmails2() {
		boolean testYn = true;
		//boolean testYn =false;
		// 1. 발송 대상 조회 (영속성 컨텍스트를 위해 트랜잭션 없이 조회하거나 필요시 분리)
		List<EmailMessage> pendingMsgs = repo.findPending(testYn);
		log.info("sendEmails");
		pendingMsgs.forEach(this::processEachEmail);
	}

	@Async // 2. 비동기 처리: 별도 스레드에서 실행
	@Transactional // 3. 각 발송 건마다 개별 트랜잭션 적용
	public void processEachEmail(EmailMessage msg) {
		try {
			// 실제 메일 발송 (I/O 작업)
			mailSender.send(msg);

			// 상태 변경
			msg.markAsSent();
			repo.save(msg);

			// 4. 영속성 컨텍스트 내에서 변경되었으므로 자동 Update 실행 (save 불필요)
			// @Transactional 덕분에 Dirty Checking이 동작하여 특정 필드만 수정됨

		} catch (Exception e) {
			log.error("메일 발송 실패 - ID: {}", msg.getId(), e);
			// 실패 시 로직 (예: 재시도 횟수 증가 등)
		}
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