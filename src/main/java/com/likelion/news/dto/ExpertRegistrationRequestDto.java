package com.likelion.news.dto;

import com.likelion.news.entity.enums.ExpertState;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExpertRegistrationRequestDto {
    private String userName;
    private String job;
    private String company;
    private String businessType;
    private String education;
    private ExpertState expertState;
    private List<String> certificationsUrl;
}
