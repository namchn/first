package com.nc.fisrt.domain.stock.adapter.out.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nc.fisrt.domain.stock.adapter.entity.EmailMessageJpaEntity;

@Repository
public interface EmailMessageJpaRepository extends JpaRepository<EmailMessageJpaEntity, Long> {

	List<EmailMessageJpaEntity> findByStatus(String status);
}