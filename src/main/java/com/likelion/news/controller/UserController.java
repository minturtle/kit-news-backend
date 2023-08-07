package com.likelion.news.controller;

import com.likelion.news.dto.ExpertRequest;
import com.likelion.news.service.ExpertService;
import io.jsonwebtoken.lang.Collections;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final ExpertService expertService;

    @GetMapping("/login/kakao")
    public void kakaoLogin(HttpServletResponse res) throws IOException {

        res.sendRedirect("/oauth2/authorization/kakao");
    }

    @PostMapping(value = "/register/expert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> registerExpert(ExpertRequest req,
                                               @RequestParam(value = "images") MultipartFile[] images) throws IOException {
        String uid = getUid().get();
        List<MultipartFile> imageList = Collections.arrayToList(images);
        expertService.registerExpert(uid, req, imageList);


        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * @author minseok kim
     * @description SecurityContextHolder에서 uid를 가져오는 메서드
     * @param
     * @return SecurityContextHolder에서 조회된 uid
     */
    private Optional<String> getUid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.isAuthenticated()){
            return Optional.of((String)authentication.getPrincipal());
        }

        return Optional.empty();
    }
}
