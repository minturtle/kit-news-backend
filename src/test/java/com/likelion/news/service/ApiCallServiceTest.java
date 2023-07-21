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

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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

    @Test
    @DisplayName("Conn객체에 HTTP Header를 주입할 수 있다.")
    void t2() throws Exception {
        //given
        Map<String, String> testHeader = new HashMap<>();

        testHeader.put("test", "1");
        testHeader.put("test2", "2");

        URL url = new URL("http://test123124214214.com");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //when
        ReflectionTestUtils.invokeMethod(apiCallService, "appendHeader", testHeader, conn);
        //then
        assertThat(conn.getRequestProperty("test")).isEqualTo("1");
        assertThat(conn.getRequestProperty("test2")).isEqualTo("2");
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

