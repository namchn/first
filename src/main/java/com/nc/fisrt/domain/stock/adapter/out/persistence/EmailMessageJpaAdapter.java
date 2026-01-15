package com.nc.fisrt.domain.stock.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.nc.fisrt.domain.stock.adapter.entity.EmailMessageJpaEntity;
import com.nc.fisrt.domain.stock.core.domain.EmailMessage;
import com.nc.fisrt.domain.stock.core.domain.SendStatus;
import com.nc.fisrt.domain.stock.core.port.out.EmailMessageRepositoryPort;

import jakarta.transaction.Transactional;

/*
Persistence Adapter (Outbound)
*/

@Component
//@RequiredArgsConstructor
public class EmailMessageJpaAdapter implements EmailMessageRepositoryPort {

	private final EmailMessageJpaRepository jpaRepository;	// 실제 Spring Data JPA

	public EmailMessageJpaAdapter(EmailMessageJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	private EmailMessage toDomain(EmailMessageJpaEntity e) {
		return new EmailMessage(
				e.getId(),
				e.getToEmail(),
				e.getSubject(),
				e.getBody(),
				SendStatus.valueOf(e.getStatus())
				);
	}

	private EmailMessageJpaEntity toEntity(EmailMessage m) {
		EmailMessageJpaEntity e = new EmailMessageJpaEntity();
		e.setId(m.getId());
		e.setToEmail(m.getToEmail());
		e.setSubject(m.getSubject());
		e.setBody(m.getBody());
		e.setStatus(m.getStatus().name());
		return e;

	        /*
	        .builder()
			.id(m.getId())
			.toEmail(m.getToEmail())
			.subject(m.getSubject())
			.body(m.getBody())
			.status(m.getStatus().name())
			.build();
	        */
	}

	
	/*
	@Override
	public void save(Member member) {
		// 도메인 모델을 DB 엔티티로 변환하여 저장
		MemberEntity entity = MemberEntity.from(member);
		jpaRepository.save(entity);
	}
	*/
	
	@Override
	public void save(EmailMessage message) {
		jpaRepository.save(toEntity(message));
	}
	
	@Override
	public EmailMessage findById(long msgId) {
			return jpaRepository.findById(msgId)
		            .map(this::toDomain)
		            .orElse(null);
		
	}

	@Override
	public List<EmailMessage> findPending() {
        return jpaRepository.findByStatus(SendStatus.PENDING.name())
                .stream()
                .map(this::toDomain)
                .toList();
    }
	
	@Override
	public List<EmailMessage> findPending(boolean testYn) {
        return jpaRepository.findByStatus(testYn?SendStatus.TEST.name():SendStatus.PENDING.name())
                .stream()
                .map(this::toDomain)
                .toList();
    }
	
	@Override
	public List<EmailMessage> findSending() {
        return jpaRepository.findByStatus(SendStatus.SENDING.name())
                .stream()
                .map(this::toDomain)
                .toList();
    }
	
	@Override
	public List<EmailMessage> findSending(boolean testYn) {
        return jpaRepository.findByStatus(testYn?SendStatus.TEST.name():SendStatus.SENDING.name())
                .stream()
                .map(this::toDomain)
                .toList();
    }

	@Override
	public void markAsSent(Long id) {
		// TODO Auto-generated method stub

	}
	
	@Override
	@Transactional
	public long updateStatusToSending(boolean testYn) {
		String targetStatus = testYn ? SendStatus.TEST.name() : SendStatus.PENDING.name();
		return jpaRepository.updateStatusToSending(targetStatus);
	}
	
	@Override
	@Transactional
	public long updateStatusToSending(String status) {
		return jpaRepository.updateStatusToSending(SendStatus.PENDING.name());
	}
	
	@Override
	@Transactional
	public long updateStatus(String status,Long id) {
		return jpaRepository.updateStatus(status, id);
	}


}
