package com.nc.fisrt.domain.batch.core.port.in;

import com.nc.fisrt.domain.batch.core.domain.Settlement;

public interface SettlementUseCase {
    void processSettlement(Settlement settlement);
    void calculate(Long orderId);
}