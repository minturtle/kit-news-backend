package com.likelion.news.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    @GetMapping("/login/kakao")
    public void kakaoLogin(HttpServletResponse res) throws IOException {

        res.sendRedirect("/oauth2/authorization/kakao");
    }


    @GetMapping("/test")
    public @ResponseBody String test() {

        return "Test";
    }

    @GetMapping("/expert/test")
    public @ResponseBody String expertTest() {

        return "expert Test";
    }

    @GetMapping("/admin/test")
    public @ResponseBody String adminTest() {

        return "admin Test";
    }
}
