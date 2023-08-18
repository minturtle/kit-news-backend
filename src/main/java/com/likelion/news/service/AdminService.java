package com.likelion.news.service;

import com.likelion.news.dto.ExpertRegistrationRequestDto;

import com.likelion.news.entity.Certification;
import com.likelion.news.entity.ExpertInfo;

import com.likelion.news.entity.enums.ExpertState;
import com.likelion.news.entity.enums.UserType;
import com.likelion.news.exception.NotFoundException.NoExpertException;
import com.likelion.news.repository.ExpertInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminService {
    private final ExpertInfoRepository expertInfoRepository;

    private static final String APPROVE = "approve";
    private static final String REJECT = "reject";

    public List<ExpertRegistrationRequestDto> getRequestedExpertRegistrations(int from, int size){
        Page<ExpertInfo> expertInfos = expertInfoRepository.findByState(ExpertState.PENDING, PageRequest.of(from / size, size, Sort.by("expertInfoId").descending()));

        return expertInfos.stream().map(expertInfo -> ExpertRegistrationRequestDto.builder()
                        .userName(expertInfo.getUser().getName())
                        .job(expertInfo.getJob())
                        .company(expertInfo.getCompany())
                        .businessType(expertInfo.getBusinessType())
                        .education(expertInfo.getEducation())
                        .expertState(expertInfo.getState())
                        .certificationsUrl(expertInfo.getCertifications().stream()
                                .map(Certification::getCertificationUrl)
                                .collect(Collectors.toList()))
                        .uid(expertInfo.getUser().getUid())
                        .build())
                .collect(Collectors.toList());
    }



    public boolean decisionRegistrations(String decision, String uid) {

        if (!APPROVE.equalsIgnoreCase(decision) && !REJECT.equalsIgnoreCase(decision)) {
            throw new IllegalArgumentException("Decision은 반드시 'approve' or 'reject' 값 이어야 합니다.");
        }

        ExpertInfo findExpertReq = expertInfoRepository.findByUserUid(uid).orElseThrow(NoExpertException::new);

        ExpertState newState = getNewState(decision);
        findExpertReq.setState(newState);

        handleDecision(newState, findExpertReq);

        return newState.equals(ExpertState.APPROVED);
    }


    private ExpertState getNewState(String decision) {
        return APPROVE.equalsIgnoreCase(decision) ? ExpertState.APPROVED : ExpertState.REJECTED;
    }

    private void handleDecision(ExpertState newState, ExpertInfo findExpertReq) {
        if (newState.equals(ExpertState.APPROVED)) {
            findExpertReq.getUser().setUserType(UserType.ROLE_EXPERT);
        }
    }



}
