package com.likelion.news.controller;

import com.likelion.news.dto.ExpertRequest;
import com.likelion.news.service.ExpertService;
import io.jsonwebtoken.lang.Collections;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "일반유저 관련 API", description = "일반 유저 관련 API입니다.")
public class UserController {

    private final ExpertService expertService;

    @Operation(
            summary = "카카오 로그인 API",
            description = "카카오 로그인 API입니다. 로그인시 토큰이 발급됩니다."
    )
    @GetMapping("/login/kakao")
    public void kakaoLogin(HttpServletResponse res) throws IOException {

        res.sendRedirect("/oauth2/authorization/kakao");
    }

    @Operation(
            summary = "전문가 신청 API",
            description = "전문가 신청 API입니다. 직업, 회사/조직, 직종, 학력, 증명서 파일이 요구됩니다."
    )
    @PostMapping(value = "/register/expert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> registerExpert(ExpertRequest req,
            @RequestPart(value = "images", required = false) MultipartFile[] images) throws IOException {
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
