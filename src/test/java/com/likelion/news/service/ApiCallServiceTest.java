package com.likelion.news.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;


@Import(ApiCallServiceTest.TestConfig.class)
@ExtendWith(SpringExtension.class)
public class ApiCallServiceTest {

    @Autowired
    private ApiCallService apiCallService;

    @Test
    @DisplayName("ApiCallService를 주입받고 테스트할 수 있다.")
    void t1() throws Exception {
        //given
        ObjectMapper objectMapper = (ObjectMapper)ReflectionTestUtils.getField(apiCallService, "objectMapper");

        //then


        assertThat(apiCallService).isNotNull();
        assertThat(objectMapper).isNotNull();
    }
    @TestConfiguration
    public static class TestConfig{


        @Bean
        public ApiCallService apiCallService(){
            return new ApiCallService(objectMapper());
        }

        @Bean
        public ObjectMapper objectMapper(){
            return new ObjectMapper();
        }
    }

}

