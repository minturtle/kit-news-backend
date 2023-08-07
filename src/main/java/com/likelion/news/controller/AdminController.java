package com.likelion.news.controller;

import com.likelion.news.dto.ExpertRegistrationRequestDto;
import com.likelion.news.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
@Tag(name = "어드민 관련 API", description = "어드민 관련 API입니다. 해당 API들은 모두 어드민 권한이 있어야 합니다.")
public class AdminController {
    private final AdminService adminService;

    @Operation(
            summary = "어드민 페이지 전문가 신청 목록 조회 API",
            description = "어드민 페이지에서 사용하는 전문가 신청 목록 조회 API입니다. from, size의 디폴트 값은 각각 0, 10입니다.")
    @GetMapping("requested")
    public List<ExpertRegistrationRequestDto> getRequestedExpertRegistrations(@RequestParam(required = false, defaultValue = "0")  @Positive Integer from,
                                                                              @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        return adminService.getRequestedExpertRegistrations(from, size);
    }

    @Operation(
            summary = "어드민 페이지 전문가 신청 승인/거절 API",
            description = "어드민 페이지에서 사용하는 전문가 신청 승인/거절 API입니다. " +
                    "uid는 전문가 신청을 한 유저의 uid이고, decision에는 approve/reject 값을 넣어주시면 됩니다."
    )
    @PatchMapping("requested/{uid}/{decision}")
    public ResponseEntity<String> decisionRegistrations(@PathVariable String decision,
                                                        @PathVariable String uid) {
        boolean isApproved = adminService.decisionRegistrations(decision, uid);

        String message = isApproved ? "승인되었습니다" : "거절되었습니다";
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

}
