package com.nc.fisrt.domain.stock.adapter.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Data
//@Builder
@Entity
@Table(name = "email_message")
public class EmailMessageJpaEntity {

    @Id @GeneratedValue
    private Long id;

    private String toEmail;
    private String subject;

    @Lob
    private String body;

    private String status;
    
    //@Enumerated(EnumType.STRING)
    //private SendStatus status; // PENDING, SENT, FAILED
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    //private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    
    
    
    /*
    // 객체 생성 시점에 바로 현재 시간 할당
    //@000Column(nullable = false, updatable = false) // updatable = false가 핵심입니다.
   
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
    
    */
    
}
