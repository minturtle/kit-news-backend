package com.likelion.news.dto.response;

import com.likelion.news.dto.NewsEmotionDto;
import com.likelion.news.entity.enums.NewsEmotionType;
import com.likelion.news.entity.enums.NewsTrustEmotionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class NewsEmotionResponse {
    private Long newsId;
    @Builder.Default
    private Map<NewsEmotionType , Integer> emotionCounts = new HashMap<>();
    private Map<NewsTrustEmotionType, Integer> trustEmotionCounts = new HashMap<>();


    private UserEmotionInfo<NewsEmotionType> userNewsEmotionInfo;
    private UserEmotionInfo<NewsTrustEmotionType> userNewsTrustEmotionInfo;


    public static NewsEmotionResponse of(Long newsId, List<NewsEmotionDto> newsEmotionDto) {
        NewsEmotionResponse resp = NewsEmotionResponse.builder()
                .newsId(newsId).build();


        for(NewsEmotionDto dto : newsEmotionDto){
            countNewsEmotion(dto, resp);

        }
        return resp;
    }

    private static void countNewsEmotion(NewsEmotionDto dto, NewsEmotionResponse resp) {
        Map<NewsEmotionType, Integer> countMap = resp.getEmotionCounts();

        if(!countMap.containsKey(dto.getEmotionType())){
            countMap.put(dto.getEmotionType(), 0);
        }
        Integer prevCount = countMap.get(dto.getEmotionType());

        countMap.put(dto.getEmotionType(), prevCount + 1);
    }
}
