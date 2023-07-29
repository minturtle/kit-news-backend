package com.likelion.news.dto;

import com.likelion.news.entity.NewsEmotion;
import com.likelion.news.entity.RefinedNews;
import com.likelion.news.entity.enums.ArticleCategory;
import com.likelion.news.entity.enums.NewsEmotionType;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
@Builder
public class RefinedNewsReadDto {

    private Long id;
    private String title;
    private String content;
    private String summary;
    private String link;
    private ArticleCategory articleCategory;
    @Builder.Default
    private Map<NewsEmotionType, Integer> emotionCounts = new HashMap<>();



    public static RefinedNewsReadDto toDto(RefinedNews entity){
        RefinedNewsReadDto dto = RefinedNewsReadDto.builder()
                .id(entity.getRefinedNewsId())
                .title(entity.getCrawledNews().getArticleTitle())
                .content(entity.getCrawledNews().getArticleContent())
                .summary(entity.getArticleSummary())
                .link(entity.getCrawledNews().getArticleLink())
                .articleCategory(entity.getCrawledNews().getArticleCategory())
                .build();

        // emotion Count 갯수 계산 및 추가
        for(NewsEmotion emotion : entity.getEmotions()){
            Map<NewsEmotionType, Integer> emotionCountMap = dto.getEmotionCounts();
            NewsEmotionType emotionType = emotion.getEmotionType();

            if(!emotionCountMap.containsKey(emotionType)){
                emotionCountMap.put(emotionType, 0);
            }
            Integer prevCount = emotionCountMap.get(emotionType);
            emotionCountMap.put(emotionType, prevCount + 1);

        }

        return dto;


    }
}
