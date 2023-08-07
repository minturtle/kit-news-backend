package com.likelion.news.dto;

import com.likelion.news.entity.NewsEmotion;
import com.likelion.news.entity.enums.NewsEmotionType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewsEmotionDto {

    private Long newsId;
    private String uid;
    private NewsEmotionType emotionType;


    public static NewsEmotionDto toDto(NewsEmotion entity){
        return NewsEmotionDto.builder()
                .newsId(entity.getRefinedNews().getRefinedNewsId())
                .uid(entity.getUser().getUid())
                .emotionType(entity.getEmotionType())
                .build();

    }

}
