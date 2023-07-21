package com.likelion.news.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@RequiredArgsConstructor
public class ApiCallService {


    private final ObjectMapper objectMapper;

    public<T> ApiServiceResponse<T> callApi(ApiServiceRequest req, Class<T> responseBodyMappingClass){
        StringBuilder result = new StringBuilder();
        URL url = new URL(req.getUrl());


        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(req.getRequestType().name());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }
        T resultBody = objectMapper.readValue(result.toString(), responseBodyMappingClass);

        ApiServiceResponse<T> resp = ApiServiceResponse.builder()
                .body(resultBody)
                .statusCode(conn.getResponseCode())
                .build();

        return resp;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class ApiServiceRequest{
        private RequestType requestType;
        private String url;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class ApiServiceResponse<T>{
        private Integer statusCode;
        private T body;
    }


    public static enum RequestType{
        GET, POST, PUT, PATCH, DELETE
    }
}

