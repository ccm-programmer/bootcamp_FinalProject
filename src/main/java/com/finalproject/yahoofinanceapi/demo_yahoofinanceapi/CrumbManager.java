package com.finalproject.yahoofinanceapi.demo_yahoofinanceapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Component;

@Component
public class CrumbManager {
    private String crumb;
    private final CookieManager cookieManager;

    public CrumbManager(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
        this.crumb = "";
    }

    public String getCrumb() throws IOException {
        cookieManager.clearCookies();
        cookieManager.refreshCookies();
        URL url = new URL("https://query1.finance.yahoo.com/v1/test/getcrumb");
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            connection.setRequestProperty("Accept", "text/plain");
            if (!cookieManager.getCookies().isEmpty()) {
                connection.setRequestProperty("Cookie", cookieManager.getCookies());
            }

            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                String error = readErrorResponse(connection);
                throw new IOException("Failed to fetch crumb: HTTP " + responseCode + " - " + error);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
            this.crumb = response.toString();
            System.out.println("Fetched Crumb: " + this.crumb); // Log crumb
            return this.crumb;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public String fetchYahooFinanceData(String symbol) throws IOException {
        String crumb = getCrumb();
        String urlString = "https://query1.finance.yahoo.com/v7/finance/quote?symbols=" + symbol + "&crumb=" + crumb;
        System.out.println("Fetching data from URL: " + urlString); // Log URL
        System.out.println("Using Cookies: " + cookieManager.getCookies()); // Log cookies used
        // ... (rest of fetchYahooFinanceData method unchanged)
        URL url = new URL(urlString);
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            connection.setRequestProperty("Accept", "application/json");
            if (!cookieManager.getCookies().isEmpty()) {
                connection.setRequestProperty("Cookie", cookieManager.getCookies());
            }

            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                String error = readErrorResponse(connection);
                throw new IOException("HTTP error code: " + responseCode + " - " + error);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
            return response.toString();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String readErrorResponse(HttpURLConnection connection) throws IOException {
        // ... (unchanged)
        StringBuilder error = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                error.append(line);
            }
        }
        return error.toString();
    }

    public String getCurrentCrumb() {
        return crumb;
    }

    public CookieManager getCookieManager() {
        return cookieManager;
    }
}