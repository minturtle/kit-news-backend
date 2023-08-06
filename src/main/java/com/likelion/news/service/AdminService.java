package com.likelion.news.service;

import com.likelion.news.dto.ExpertRegistrationRequestDto;

import com.likelion.news.entity.Certification;
import com.likelion.news.entity.ExpertInfo;

import com.likelion.news.entity.enums.ExpertState;
import com.likelion.news.exception.ClientException;
import com.likelion.news.exception.NotFoundException.NoExpertException;
import com.likelion.news.repository.CertificationRepository;
import com.likelion.news.repository.ExpertInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminService {
    private final ExpertInfoRepository expertInfoRepository;

    public List<ExpertRegistrationRequestDto> getRequestedExpertRegistrations(Pageable pageable){
        Page<ExpertInfo> expertInfos = expertInfoRepository.findByState(ExpertState.PENDING, pageable);

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

    public boolean decisionRegistrations(String decision, ExpertRegistrationRequestDto req){
        ExpertInfo findExpertReq = expertInfoRepository.findByUserUid(req.getUid());

        if (findExpertReq == null) {
            throw new NoExpertException();
        }

        Map<String, ExpertState> decisionMap = Map.of(
                "approve", ExpertState.APPROVED,
                "reject", ExpertState.REJECTED
        );

        ExpertState newState = decisionMap.get(decision.toLowerCase());

        if (newState == null) {
            throw new ClientException("Decision 반드시 'approve' or 'reject' 값 이어야 합니다.");
        }

        findExpertReq.setState(newState);

        return newState.equals(ExpertState.APPROVED);
    }


}
