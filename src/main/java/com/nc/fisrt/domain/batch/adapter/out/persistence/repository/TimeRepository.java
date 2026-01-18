package com.nc.fisrt.domain.batch.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nc.fisrt.domain.batch.adapter.entity.TimeEntity;

@Repository
public interface TimeRepository extends JpaRepository<TimeEntity, Long> {
}