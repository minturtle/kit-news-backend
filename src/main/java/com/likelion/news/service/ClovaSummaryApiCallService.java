package com.likelion.news.service;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ClovaSummaryApiCallService {

    private final ApiCallService apiCallService;
    private final Environment environment;

    private final String CLIENT_ID_HEADER = "X-NCP-APIGW-API-KEY-ID";
    private final String CLIENT_SECRET_HEADER = "X-NCP-APIGW-API-KEY";


    protected ClovaSummaryApiCallService(ApiCallService apiCallService, Environment environment) {
        this.apiCallService = apiCallService;
        this.environment = environment;
    }




}
