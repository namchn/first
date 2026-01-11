package com.nc.fisrt.domain.stock.core.domain;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;

//domain/StockReport.java
@Getter
@Builder
public class StockReport {
	private final String symbol;
	private final double currentPrice;
	private final Map<Integer, Double> movingAverages; // 2, 3, 5, 10, 200, 220일선 등
	private final String analysisDate;

	public double getPriceRatio(int period) {
		return (currentPrice / movingAverages.get(period)) * 100;
	}
}