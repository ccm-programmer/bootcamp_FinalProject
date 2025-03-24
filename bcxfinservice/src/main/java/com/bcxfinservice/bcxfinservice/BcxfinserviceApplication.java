package com.bcxfinservice.bcxfinservice;

import com.bcxfinservice.bcxfinservice.service.StockSymbolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BcxfinserviceApplication implements CommandLineRunner {

	@Autowired
	private StockSymbolService stockSymbolService;

	@Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String STOCK_SYMBOLS_KEY = "stock_symbols"; // 與 StockSymbolService 中的鍵一致

    public static void main(String[] args) {
        SpringApplication.run(BcxfinserviceApplication.class, args);
    }

  @Override
	public void run(String... args) throws Exception {
			// 在啟動時清除 Redis 中的 stock_symbols 鍵
			redisTemplate.delete(STOCK_SYMBOLS_KEY);
			System.out.println("Redis key '" + STOCK_SYMBOLS_KEY + "' cleared on server startup.");

			// 初始化股票符號（這會從資料庫重新載入並存入 Redis）
			stockSymbolService.initStockSymbols();
			System.out.println("Stock symbols initialized: " + stockSymbolService.getStockSymbols());
}
}