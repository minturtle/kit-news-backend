package com.likelion.news.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;

class ShellRunnerServiceTest {

    private ShellRunnerService shellRunnerService = new ShellRunnerService();

    @Test
    @DisplayName("command와 arg를 받아 실제 실행하는 명령어를 만들 수 있다.")
    void t1() throws Exception {
        //given
        String testCommand = "echo";
        String[] testArgs = new String[]{"hello"};
        //when
        String result = (String)(ReflectionTestUtils
                .invokeMethod(shellRunnerService, "createExecuteCommand", testCommand, testArgs));
        //then
        assertThat(result).isEqualTo("echo hello");

    }


    @Test
    @DisplayName("command와 arg를 받아 실제 실행하는 명령어를 만들 수 있다. 이 때, arg를 여러개 줄 수 있다.")
    void t2() throws Exception {
        //given
        String testCommand = "echo";
        String[] testArgs = new String[]{"hello", "world"};
        //when
        String result = (String)(ReflectionTestUtils
                .invokeMethod(shellRunnerService, "createExecuteCommand", testCommand, testArgs));
        //then
        assertThat(result).isEqualTo("echo hello world");
    }



}