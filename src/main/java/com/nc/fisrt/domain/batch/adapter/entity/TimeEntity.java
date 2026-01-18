package com.nc.fisrt.domain.batch.adapter.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ⏰ 현재 시간을 저장하는 Entity
 */
@Entity
@Table(name = "time_log")
@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
public class TimeEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime time;

    public static TimeEntity ofNow() {
        return new TimeEntity(null, LocalDateTime.now());
    }
    
    public TimeEntity(LocalDateTime time) {
        this.time = time;
    }
}