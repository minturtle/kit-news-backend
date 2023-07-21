package com.likelion.news.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
@Slf4j
public class ShellRunnerService {

    public String executeShell(String command, String ... args){
        try{
            return proceedShellScript(command, args);
        }catch (IOException e){
            log.error("Execute Shell Failed");
            e.printStackTrace();
            // TODO Exception Throw로 바꿔줘야함.
            return null;
        }
    }

    /**
    * @methodName proceedShellScrip
    * Author : Minseok Kim
    * @description 쉘 명령어를 수행하는 메서드
    *
    * @param command 쉘 명령어
    * @return 쉘이 stdout한 문자열
    * @exception IOException Shell 명령어 실행에 실패했을 경우
     */
    private String proceedShellScript(String command, String ... args) throws IOException {
        String executeCommand = createExecuteCommand(command, args);

        Process process = Runtime.getRuntime().exec(executeCommand);

        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder processResultBuilder = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            processResultBuilder.append(line);
            if(in.ready()){
                processResultBuilder.append("\n");
            }

        }
        return processResultBuilder.toString();
    }


    /**
    * @methodName createExecuteCommand
    * Author : Minseok Kim
    * @description command와 args를 입력받아 실행할 쉘 명령어를 만듭니다.
    *
    * @param command 쉘 스크립트 명령어
    * @param args 쉘 스크립트 명령어에 대한 Arguments
    * @return 완성된 쉘 스크립트 명령어
     */
    private String createExecuteCommand(String command, String ... args) {
        if(args == null || args.length == 0){
            return command;
        }

        StringBuilder executeCommandBuilder = new StringBuilder(command);

        for(String arg : args){
            executeCommandBuilder.append(" ");
            executeCommandBuilder.append(arg);
        }

        return executeCommandBuilder.toString();
    }
}
