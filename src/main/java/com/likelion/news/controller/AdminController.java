package com.likelion.news.controller;

import com.likelion.news.dto.ExpertRegistrationRequestDto;
import com.likelion.news.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("requested-experts")
    public List<ExpertRegistrationRequestDto> getRequestedExpertRegistrations(Pageable pageable) {
        return adminService.getRequestedExpertRegistrations(pageable);
    }
}
