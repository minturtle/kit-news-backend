package com.likelion.news.dto;


import com.likelion.news.entity.enums.NewsTrustEmotionType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewsTrustEmotionDto {

    private NewsTrustEmotionType trustEmotionType;
    private String uid;
}
