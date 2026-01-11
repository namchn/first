package com.nc.fisrt.domain.stock.core.port.out;

//application/port/out/LogPersistencePort.java (로그 저장용)
public interface LogPersistencePort {
	void recordApiLog(String symbol, double time);
}