package com.bcxfinservice.bcxfinservice.repository;

// 創建 StockSymbolRepository 用於資料庫操作。

import com.bcxfinservice.bcxfinservice.entity.StockSymbol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockSymbolRepository extends JpaRepository<StockSymbol, String> {
}
