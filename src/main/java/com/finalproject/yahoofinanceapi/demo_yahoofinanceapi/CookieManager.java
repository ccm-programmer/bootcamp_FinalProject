package com.finalproject.yahoofinanceapi.demo_yahoofinanceapi;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class CookieManager {
    private String cookies;

    public CookieManager() {
        this.cookies = "";
    }

    public void refreshCookies() throws IOException {
        URL url = new URL("https://finance.yahoo.com");
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Map<String, List<String>> headers = connection.getHeaderFields();
                List<String> cookieHeaders = headers.get("Set-Cookie");
                if (cookieHeaders != null && !cookieHeaders.isEmpty()) {
                    StringBuilder cookieBuilder = new StringBuilder();
                    for (String cookie : cookieHeaders) {
                        if (cookieBuilder.length() > 0) {
                            cookieBuilder.append("; ");
                        }
                        cookieBuilder.append(cookie.split(";", 2)[0]);
                    }
                    this.cookies = cookieBuilder.toString();
                    System.out.println("Fetched Cookies: " + this.cookies); // Log cookies
                } else {
                    this.cookies = "";
                    System.out.println("No cookies received from https://finance.yahoo.com");
                }
            } else {
                throw new IOException("Failed to fetch cookies: HTTP " + responseCode);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public void clearCookies() {
        this.cookies = "";
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }
}
