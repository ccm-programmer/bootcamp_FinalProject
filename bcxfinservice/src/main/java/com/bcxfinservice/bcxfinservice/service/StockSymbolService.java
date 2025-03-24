package com.bcxfinservice.bcxfinservice.service;

// 創建一個服務類 StockSymbolService，負責載入股票符號並處理 Redis 快取。

import com.bcxfinservice.bcxfinservice.entity.StockSymbol;
import com.bcxfinservice.bcxfinservice.repository.StockSymbolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class StockSymbolService {

    @Autowired
    private StockSymbolRepository stockSymbolRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String STOCK_SYMBOLS_KEY = "stock_symbols";
    private static final long CACHE_TTL = 24 * 60 * 60; // 24 hours in seconds

    // 模擬股票符號清單（實際應用中可從檔案或 API 載入）
    private List<StockSymbol> getInitialStockSymbols() {
        return Arrays.asList(
            new StockSymbol("AAPL", "Apple Inc."),
            new StockSymbol("GOOGL", "Alphabet Inc."),
            new StockSymbol("MSFT", "Microsoft Corporation")
        );
    }

    // 初始化股票符號到資料庫
    public void initStockSymbols() {
        List<StockSymbol> initialSymbols = getInitialStockSymbols();
        stockSymbolRepository.saveAll(initialSymbols);
            }

    // 從 Redis 或資料庫獲取股票符號清單
    @SuppressWarnings("unchecked")
    public List<StockSymbol> getStockSymbols() {
        // 嘗試從 Redis 獲取
        List<Object> cachedSymbols = redisTemplate.opsForList().range(STOCK_SYMBOLS_KEY, 0, -1);
        if (cachedSymbols != null && !cachedSymbols.isEmpty()) {
            return (List<StockSymbol>) (List<?>) cachedSymbols; // 強制轉型
        }

        // 如果 Redis 無數據，從資料庫獲取
        List<StockSymbol> symbols = stockSymbolRepository.findAll();
        if (!symbols.isEmpty()) {
            // 存入 Redis，設定 24 小時過期
            redisTemplate.opsForList().rightPushAll(STOCK_SYMBOLS_KEY, symbols.toArray());
            redisTemplate.expire(STOCK_SYMBOLS_KEY, CACHE_TTL, TimeUnit.SECONDS);
        }
        return symbols;
    }
}
