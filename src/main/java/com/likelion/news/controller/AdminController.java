package com.likelion.news.controller;

import com.likelion.news.dto.ExpertRegistrationRequestDto;
import com.likelion.news.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("requested")
    public List<ExpertRegistrationRequestDto> getRequestedExpertRegistrations(Pageable pageable) {
        return adminService.getRequestedExpertRegistrations(pageable);
    }

    @PatchMapping("requested/{uid}/{decision}")
    public ResponseEntity<String> decisionRegistrations(@PathVariable String decision,
                                                        @PathVariable String uid) {
        boolean isApproved = adminService.decisionRegistrations(decision, uid);

        String message = isApproved ? "승인되었습니다" : "거절되었습니다";
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

}
