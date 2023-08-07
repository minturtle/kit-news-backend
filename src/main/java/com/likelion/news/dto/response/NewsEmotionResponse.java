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

        UserEmotionInfo<NewsEmotionType> userEmotionInfo = checkUserClickedEmotion(newsEmotionDtoList, uid);
        UserEmotionInfo<NewsTrustEmotionType> trustUserEmotionInfo = checkUserClickedTrustEmotion(newsTrustEmotionDtoList, uid);


        NewsEmotionResponse resp = NewsEmotionResponse.builder()
                .newsId(newsId)
                .userNewsEmotionInfo(userEmotionInfo)
                .userNewsTrustEmotionInfo(trustUserEmotionInfo)
                .build();



        //init count map
        for(NewsEmotionType  emotionType : NewsEmotionType.values()){
            resp.getEmotionCounts().put(emotionType, 0);

        }
        // emotionType의 갯수 측정
        for(NewsEmotionDto dto : newsEmotionDtoList){
            countNewsEmotion(dto, resp.getEmotionCounts());
        }


        // init trust count map
        for(NewsTrustEmotionType trustEmotionType : NewsTrustEmotionType.values()){
            resp.getTrustEmotionCounts().put(trustEmotionType, 0);
        }

        //emotionTrustType의 갯수 측정
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
            if(!dto.getUid().equals(uid.get())){
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
            if(!dto.getUid().equals(uid.get())){
                continue;
            }
           emotionInfo.setUserClicked(true);
           emotionInfo.setUserClickEmotionType(dto.getEmotionType());
        }


        return emotionInfo;
    }


    private static void countNewsTrustEmotion(NewsTrustEmotionDto dto, Map<NewsTrustEmotionType, Integer> trustEmotionCountMap) {
        Integer prevCount = trustEmotionCountMap.get(dto.getTrustEmotionType());

        trustEmotionCountMap.put(dto.getTrustEmotionType(), prevCount + 1);
    }


    private static void countNewsEmotion(NewsEmotionDto dto, Map<NewsEmotionType, Integer> emotionCountMap) {
        Integer prevCount = emotionCountMap.get(dto.getEmotionType());

        emotionCountMap.put(dto.getEmotionType(), prevCount + 1);
    }
}
