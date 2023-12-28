package com.likelion.news.dto;

import com.likelion.news.entity.enums.ExpertState;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDto {
    private String name;
    private String email;
    private String nickname;
    private ExpertState expertState;
}
