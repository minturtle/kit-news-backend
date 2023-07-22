package com.likelion.news.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;


@SpringBootTest(classes = {ClovaSummaryApiCallService.class, ApiCallService.class, ObjectMapper.class})
@ExtendWith(SpringExtension.class)
class ClovaSummaryApiCallServiceTest {
    private ClientAndServer mockServer;

    @Autowired
    private ClovaSummaryApiCallService clovaSummaryApiCallService;

    @MockBean
    private Environment environment;


    @BeforeEach
    void setUp() {
        mockServer = ClientAndServer.startClientAndServer(8888);
        mockServer
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/test")
                                .withHeader(Header.header("test", "test-header"))
                                .withBody("{\"test1\":\"1\",\"test2\":\"2\"}")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeader(new Header("Content-Type", "text/xml;charset=utf-8"))
                                .withBody("{ \"test\" : \"good\"}")
                );
    }


    @AfterEach
    void tearDown() {
        mockServer.stop();
    }


    @Test
    @DisplayName("테스트에 필요한 의존관계를 모두 주입받고 사용할 수 있다.")
    void t1() throws Exception {
        //given
        Object injectedBean1 = ReflectionTestUtils.getField(clovaSummaryApiCallService, "apiCallService");
        Object injectedBean2 = ReflectionTestUtils.getField(clovaSummaryApiCallService, "env");

        //then
        assertThat(clovaSummaryApiCallService).isNotNull();
        assertThat(injectedBean1).isNotNull();
        assertThat(injectedBean2).isNotNull();
    }


}