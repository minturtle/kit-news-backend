package com.likelion.news.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDto {
    private String name;
    private String email;
    private String nickname;

}
