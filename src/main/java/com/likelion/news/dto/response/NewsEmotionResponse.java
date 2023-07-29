package com.likelion.news.dto.response;

import com.likelion.news.entity.enums.NewsEmotionType;
import lombok.Builder;

import java.util.HashMap;
import java.util.Map;


@Builder
public class NewsEmotionResponse {
    private Long newsId;
    private Map<NewsEmotionType , Integer> emotionCounts = new HashMap<>();
}
