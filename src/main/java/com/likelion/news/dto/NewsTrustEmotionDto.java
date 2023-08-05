package com.likelion.news.dto;


import com.likelion.news.entity.NewsTrustEmotion;
import com.likelion.news.entity.enums.NewsTrustEmotionType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewsTrustEmotionDto {

    private Long newsId;
    private NewsTrustEmotionType trustEmotionType;
    private String uid;

    public static  NewsTrustEmotionDto toDto(NewsTrustEmotion newsTrustEmotion) {
        return NewsTrustEmotionDto.builder()
                .newsId(newsTrustEmotion.getRefinedNews().getRefinedNewsId())
                .trustEmotionType(newsTrustEmotion.getTrustEmotionType())
                .uid(newsTrustEmotion.getUser().getUid())
                .build();
    }
}
