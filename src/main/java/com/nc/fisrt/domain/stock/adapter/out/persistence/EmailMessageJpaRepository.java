package com.nc.fisrt.domain.stock.adapter.out.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nc.fisrt.domain.stock.adapter.entity.EmailMessageJpaEntity;

@Repository
public interface EmailMessageJpaRepository extends JpaRepository<EmailMessageJpaEntity, Long> {

	// 1. 발송 대기 건을 조회 즉시 'SENDING'으로 변경 (정합성 보장)
    @Modifying
    @Query("UPDATE EmailMessageJpaEntity e SET e.status = 'SENDING' WHERE  e.status = :status")
    long updateStatusToSending(@Param("status") String status);


	// 2. 'SENDING' 상태인 것만 가져오기
	List<EmailMessageJpaEntity> findByStatus(String status);
	
	
    // 3. 발송 완료 후 특정 필드만 즉시 업데이트 (성능 최적화)
    @Modifying
    @Query("UPDATE EmailMessageJpaEntity e SET e.status = :status , e.sentAt = NOW() WHERE e.id = :id")
    long updateStatus(@Param("status") String status
    				   ,@Param("id") Long id);
	
}