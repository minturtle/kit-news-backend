package com.likelion.news.dto.response;


import com.likelion.news.dto.CommentDto;
import com.likelion.news.dto.RefinedNewsReadDto;
import com.likelion.news.entity.CommentEmotion;
import com.likelion.news.entity.enums.ArticleCategory;
import com.likelion.news.entity.enums.CommentEmotionType;
import com.likelion.news.entity.enums.NewsEmotionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
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

        private String content;

        private String expertName;

        @Builder.Default
        private Map<CommentEmotionType, Integer> emotionCounts = new HashMap<>();


        public static CommentResponse of(CommentDto comment){
            CommentResponse res = CommentResponse.builder()
                    .expertUid(comment.getExpertUid())
                    .expertProfileImage(comment.getExpertProfileImage())
                    .content(comment.getContent())
                    .expertName(comment.getExpertName()).build();

            // Comment 감정의 갯수 측정
            for(CommentDto.CommentEmotionDto commentEmotion :  comment.getCommentEmotions()){
                Map<CommentEmotionType, Integer> commentEmotionCounts = res.getEmotionCounts();

                if(!commentEmotionCounts.containsKey(commentEmotion.getEmotionType())){
                    commentEmotionCounts.put(commentEmotion.getEmotionType(), 0);
                }
                Integer prevCount = commentEmotionCounts.get(commentEmotion.getEmotionType());

                commentEmotionCounts.put(commentEmotion.getEmotionType(), prevCount+1);

            }


            return res;
        }

    }

}
