package com.likelion.news.dto;

import com.likelion.news.entity.NewsEmotion;
import com.likelion.news.entity.enums.NewsEmotionType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewsEmotionDto {

    private Long newsId;
    private Long userId;
    private NewsEmotionType emotionType;


    public static NewsEmotionDto toDto(NewsEmotion entity){
        return NewsEmotionDto.builder()
                .newsId(entity.getRefinedNews().getRefinedNewsId())
                .userId(entity.getUser().getId())
                .emotionType(entity.getEmotionType())
                .build();

    }

}
