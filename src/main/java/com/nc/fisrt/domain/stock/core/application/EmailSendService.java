package com.nc.fisrt.domain.stock.core.application;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nc.fisrt.domain.stock.adapter.entity.EmailMessageJpaEntity;
import com.nc.fisrt.domain.stock.adapter.scheduler.EmailSendScheduler;
import com.nc.fisrt.domain.stock.core.domain.EmailMessage;
import com.nc.fisrt.domain.stock.core.port.in.SendPendingEmailsUseCase;
import com.nc.fisrt.domain.stock.core.port.out.EmailMessageRepositoryPort;
import com.nc.fisrt.domain.stock.core.port.out.MailSenderPort;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailSendService implements SendPendingEmailsUseCase {

    private final EmailMessageRepositoryPort repo;
    private final MailSenderPort mailSender;



    @Override
   	@Async // 2. 비동기 처리: 별도 스레드에서 실행
   	@Transactional // 3. 각 발송 건마다 개별 트랜잭션 적용
	public void processEachEmail(long msgId) {
		EmailMessage msg = repo.findById(msgId);
		// 1. null 체크
		if (msg == null) {
			log.warn("메시지를 찾을 수 없습니다. ID: {}", msgId);
			return;
		}

		try {
			mailSender.send(msg); // 메일 발송
			msg.markAsSent(); // 상태 변경 (Dirty Checking 작동!)
			repo.save(msg); // 이제 호출 안 해도 자동으로 반영됨
		} catch (Exception e) {
			log.error("발송 실패", e);
		}

	}
    
    @Override
	@Async // 2. 비동기 처리: 별도 스레드에서 실행
	@Transactional // 3. 각 발송 건마다 개별 트랜잭션 적용
	public void processEachEmail(EmailMessage msg) {
		try {
			// 실제 메일 발송 (I/O 작업)
			mailSender.send(msg);

			// 상태 변경
			msg.markAsSent();
			//repo.save(msg);

			// 4. 영속성 컨텍스트 내에서 변경되었으므로 자동 Update 실행 (save 불필요)
			// @Transactional 덕분에 Dirty Checking이 동작하여 특정 필드만 수정됨

		} catch (Exception e) {
			log.error("메일 발송 실패 - ID: {}", msg.getId(), e);
			// 실패 시 로직 (예: 재시도 횟수 증가 등)
		}
	}
    
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