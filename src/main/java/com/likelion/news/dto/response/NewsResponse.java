package com.likelion.news.dto.response;


import com.likelion.news.dto.CommentDto;
import com.likelion.news.dto.RefinedNewsReadDto;
import com.likelion.news.entity.CommentEmotion;
import com.likelion.news.entity.enums.ArticleCategory;
import com.likelion.news.entity.enums.NewsEmotionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Data
public class NewsResponse {

    private String link;
    private ArticleCategory articleCategory;
    private String title;
    private String summary;
    private String content;
    private Map<NewsEmotionType, Integer> emotionCounts;
    private List<CommentResponse> comments;

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @Data
    public static class CommentResponse{
        private String expertUid;

        private String expertProfileImage;

        private String expertName;

        private Map<CommentEmotion, Integer> emotionCounts;

    }

}
