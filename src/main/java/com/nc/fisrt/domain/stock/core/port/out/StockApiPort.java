package com.nc.fisrt.domain.stock.core.port.out;

import com.nc.fisrt.domain.stock.core.domain.StockReport;

public interface StockApiPort {
    StockReport fetchStockData();
}