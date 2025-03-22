package com.finalproject.yahoofinanceapi.demo_yahoofinanceapi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class YahooFinanceController {

    private final CrumbManager crumbManager;

    public YahooFinanceController(CrumbManager crumbManager) {
        this.crumbManager = crumbManager;
    }

    @GetMapping("/quote")
    public ResponseEntity<String> getQuote(@RequestParam String symbol) {
        try {
            String data = crumbManager.fetchYahooFinanceData(symbol);
            return ResponseEntity.ok(data);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error fetching data: " + e.getMessage());
        }
    }
}