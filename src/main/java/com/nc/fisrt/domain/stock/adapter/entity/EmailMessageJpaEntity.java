package com.nc.fisrt.domain.stock.adapter.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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

    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
}
