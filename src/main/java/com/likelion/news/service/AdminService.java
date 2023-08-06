package com.likelion.news.service;

import com.likelion.news.dto.ExpertRegistrationRequestDto;

import com.likelion.news.entity.Certification;
import com.likelion.news.entity.ExpertInfo;

import com.likelion.news.entity.enums.ExpertState;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminService {
    private final ExpertInfoRepository expertInfoRepository;
    private final CertificationRepository certificationRepository;

    public List<ExpertRegistrationRequestDto> getRequestedExpertRegistrations(Pageable pageable){
        Page<ExpertInfo> expertInfos = expertInfoRepository.findByState(ExpertState.PENDING, pageable);

        log.info("개수 : {}", expertInfos.getSize());

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
                        .build())
                .collect(Collectors.toList());
    }

}
