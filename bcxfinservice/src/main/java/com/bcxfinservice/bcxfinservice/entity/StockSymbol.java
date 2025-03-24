package com.bcxfinservice.bcxfinservice.entity;

// 創建一個名為 StockSymbol 的 JPA 實體，代表資料庫中的股票符號表。
// @Entity：標記為 JPA 實體。
// @Id：將 symbol 設為主鍵。
// @Data：Lombok 注解，自動生成 getter、setter 等。

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class StockSymbol {
    @Id
    private String symbol;
    private String name;

    // 添加帶參數的構造函數
    public StockSymbol(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    // 無參構造函數（JPA 要求）
    public StockSymbol() {
    }
}
