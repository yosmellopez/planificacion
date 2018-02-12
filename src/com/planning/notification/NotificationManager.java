package com.planning.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriUtils;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class NotificationManager {
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    public <T> T getForObject(String url, Map<String, Object> body, Class<T> response, HttpHeaders headers, Map<String, Object> parameters) {
        return executeRequest(url, HttpMethod.GET, body, response, headers, parameters);
    }
    
    public <T> T getForObject(String url, Map<String, Object> body, Class<T> response, HttpHeaders headers) {
        return executeRequest(url, HttpMethod.GET, body, response, headers, new HashMap<>());
    }
    
    public <T> T getForObject(String url, Class<T> response, HttpHeaders headers) {
        return executeRequest(url, HttpMethod.GET, null, response, headers, new HashMap<>());
    }
    
    public <T> T getForObject(String url, Class<T> response) {
        return executeRequest(url, HttpMethod.GET, null, response, new HttpHeaders(), new HashMap<>());
    }
    
    public <T> T postForObject(String url, Map<String, Object> body, Class<T> response, HttpHeaders headers, Map<String, Object> parameters) {
        return executeRequest(url, HttpMethod.POST, body, response, headers, parameters);
    }
    
    public <T> T postForObject(String url, Map<String, Object> body, Class<T> response, HttpHeaders headers) {
        return executeRequest(url, HttpMethod.POST, body, response, headers, new HashMap<>());
    }
    
    public <T> T postForObject(String url, Class<T> response, HttpHeaders headers) {
        return executeRequest(url, HttpMethod.POST, null, response, headers, new HashMap<>());
    }
    
    public <T> T postForObject(String url, Class<T> response) {
        return executeRequest(url, HttpMethod.POST, null, response, new HttpHeaders(), new HashMap<>());
    }
    
    public <T> T postForObject(String url, Class<T> response, Map<String, Object> body) {
        return executeRequest(url, HttpMethod.POST, body, response, new HttpHeaders(), new HashMap<>());
    }
    
    private <T> T executeRequest(String url, HttpMethod method, Map<String, Object> body, Class<T> response, HttpHeaders headers, Map<String, Object> parameters) {
        try {
            URL uri = new URL(expandUrl(url, parameters));
            HttpURLConnection con = (HttpURLConnection) uri.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic MTYzNDAxNzEtZTIwYi00NDY5LTk4NDgtZjJkNzAyZTJjMzE4");
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                String key = entry.getKey();
                List<String> value = entry.getValue();
                String collect = value.stream().collect(Collectors.joining());
                con.setRequestProperty(key, collect);
            }
            con.setRequestMethod(method.name());
            String jsonString = objectMapper.writeValueAsString(body);
            byte[] bytes = jsonString.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(bytes.length);
            OutputStream outputStream = con.getOutputStream();
            outputStream.write(bytes);
            
            int httpResponse = con.getResponseCode();
            System.out.println("httpResponse: " + httpResponse);
            String jsonResponse = "";
            if (httpResponse >= HttpURLConnection.HTTP_OK && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                try (Scanner scanner = new Scanner(con.getInputStream(), "UTF-8")) {
                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                }
            } else {
                try (Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8")) {
                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, jsonResponse);
                }
            }
            T object = objectMapper.readValue(jsonResponse, response);
            return object;
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private String expandUrl(String url, Map<String, Object> parameters) throws UnsupportedEncodingException {
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            url += key + "=" + UriUtils.encode(String.valueOf(value), "UTF-8");
        }
        return url;
    }
}
