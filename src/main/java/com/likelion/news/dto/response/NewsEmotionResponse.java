package com.likelion.news.dto.response;

import com.likelion.news.dto.NewsEmotionDto;
import com.likelion.news.dto.NewsTrustEmotionDto;
import com.likelion.news.entity.enums.NewsEmotionType;
import com.likelion.news.entity.enums.NewsTrustEmotionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class NewsEmotionResponse {
    private Long newsId;


    @Builder.Default
    private Map<NewsEmotionType , Integer> emotionCounts = new HashMap<>();
    @Builder.Default
    private Map<NewsTrustEmotionType, Integer> trustEmotionCounts = new HashMap<>();


    private UserEmotionInfo<NewsEmotionType> userNewsEmotionInfo;
    private UserEmotionInfo<NewsTrustEmotionType> userNewsTrustEmotionInfo;


    public static NewsEmotionResponse of(
            Long newsId,
            List<NewsEmotionDto> newsEmotionDtoList,
            List<NewsTrustEmotionDto> newsTrustEmotionDtoList,
            Optional<String> uid
    ) {

        checkUserClickedEmotion(newsEmotionDtoList, uid);
        checkUserClickedTrustEmotion(newsTrustEmotionDtoList, uid);


        NewsEmotionResponse resp = NewsEmotionResponse.builder()
                .newsId(newsId).build();


        for(NewsEmotionDto dto : newsEmotionDtoList){
            countNewsEmotion(dto, resp.getEmotionCounts());

        }

        for(NewsTrustEmotionDto dto : newsTrustEmotionDtoList){
            countNewsTrustEmotion(dto, resp.getTrustEmotionCounts());
        }

        return resp;
    }

    private static UserEmotionInfo<NewsTrustEmotionType> checkUserClickedTrustEmotion(List<NewsTrustEmotionDto> newsTrustEmotionDtoList, Optional<String> uid) {
        UserEmotionInfo<NewsTrustEmotionType> emotionInfo = UserEmotionInfo.<NewsTrustEmotionType>builder()
                .build();


        if(uid.isEmpty()){
            return emotionInfo;
        }

        // 탐색 성공시 userClick을 true로 설정
        for(NewsTrustEmotionDto dto : newsTrustEmotionDtoList){
            if(dto.getUid().equals(uid.get())){
                continue;
            }
            emotionInfo.setUserClicked(true);
            emotionInfo.setUserClickEmotionType(dto.getTrustEmotionType());
        }


        return emotionInfo;


    }

    private static UserEmotionInfo<NewsEmotionType> checkUserClickedEmotion(List<NewsEmotionDto> newsEmotionDtoList, Optional<String> uid) {
        UserEmotionInfo<NewsEmotionType> emotionInfo = UserEmotionInfo.<NewsEmotionType>builder()
                .build();

        if(uid.isEmpty()){
            return emotionInfo;
        }

        // 탐색 성공시 userClick을 true로 설정
       for(NewsEmotionDto dto : newsEmotionDtoList){
            if(dto.getUid().equals(uid.get())){
                continue;
            }
           emotionInfo.setUserClicked(true);
           emotionInfo.setUserClickEmotionType(dto.getEmotionType());
        }


        return emotionInfo;
    }


    private static void countNewsTrustEmotion(NewsTrustEmotionDto dto, Map<NewsTrustEmotionType, Integer> trustEmotionCountMap) {
        if(!trustEmotionCountMap.containsKey(dto.getTrustEmotionType())){
            trustEmotionCountMap.put(dto.getTrustEmotionType(), 0);
        }
        Integer prevCount = trustEmotionCountMap.get(dto.getTrustEmotionType());

        trustEmotionCountMap.put(dto.getTrustEmotionType(), prevCount + 1);
    }


    private static void countNewsEmotion(NewsEmotionDto dto, Map<NewsEmotionType, Integer> emotionCountMap) {

        if(!emotionCountMap.containsKey(dto.getEmotionType())){
            emotionCountMap.put(dto.getEmotionType(), 0);
        }
        Integer prevCount = emotionCountMap.get(dto.getEmotionType());

        emotionCountMap.put(dto.getEmotionType(), prevCount + 1);
    }
}
