package com.likelion.news.dto;

import com.likelion.news.entity.NewsEmotion;
import com.likelion.news.entity.enums.ArticleCategory;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
public class RefinedNewsReadDto {

    private Long id;
    private String title;
    private String content;
    private String summary;
    private String link;
    private ArticleCategory articleCategory;
    private List<EmotionCount> emotionCounts;

    @Data
    @Builder
    public static class EmotionCount{
        private NewsEmotion emotion;
        private Integer count;
    }
}
