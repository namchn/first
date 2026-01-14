package com.nc.fisrt.domain.stock.core.port.in;

import reactor.core.publisher.Mono;

//[Inbound Port/UseCase] 외부(Web)가 도메인에 요청하는 인터페이스
public interface GetStockReportUseCase {

	Mono<String> getStockReport(boolean testYn, String symbol, String apiKey);

	//void register(RegisterCommand command);
}
