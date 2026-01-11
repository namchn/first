package com.nc.fisrt.domain.stock.core.port.in;

import reactor.core.publisher.Mono;

public interface GetStockReportUseCase {

	Mono<String> getStockReport(boolean testYn, String symbol, String apiKey);

}
