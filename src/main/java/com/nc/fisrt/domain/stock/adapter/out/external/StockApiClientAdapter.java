package com.nc.fisrt.domain.stock.adapter.out.external;

import org.springframework.stereotype.Component;

import com.nc.fisrt.domain.stock.core.domain.StockReport;
import com.nc.fisrt.domain.stock.core.port.out.StockApiPort;

@Component
public class StockApiClientAdapter implements StockApiPort {

	@Override
	public StockReport fetchStockData() {
		// TODO Auto-generated method stub
		return null;
	}
    // 외부 API 호출 구현
}