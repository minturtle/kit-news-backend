package com.likelion.news.dto.response;


import com.likelion.news.dto.CommentDto;
import com.likelion.news.entity.enums.CommentEmotionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Data
public class CommentResponse{
    private Long commentId;
    private String expertUid;

    private String expertProfileImage;

    private String content;

    private String expertName;

    @Builder.Default
    private Map<CommentEmotionType, Integer> emotionCounts = new HashMap<>();


    private UserEmotionInfo<CommentEmotionType> userEmotionInfo;

    public static CommentResponse of(CommentDto comment, String uid){
        UserEmotionInfo<CommentEmotionType> userEmotionInfo = UserEmotionInfo.<CommentEmotionType>builder().build();


        CommentResponse res = CommentResponse.builder()
                .commentId(comment.getId())
                .expertUid(comment.getExpertUid())
                .expertProfileImage(comment.getExpertProfileImage())
                .content(comment.getContent())
                .expertName(comment.getExpertName())
                .userEmotionInfo(userEmotionInfo)
                .build();




        // Comment 감정의 갯수 측정
        for(CommentEmotionType type : CommentEmotionType.values()){
            res.getEmotionCounts().put(type, 0);
        }


        for(CommentDto.CommentEmotionDto commentEmotion :  comment.getCommentEmotions()){
            Map<CommentEmotionType, Integer> commentEmotionCounts = res.getEmotionCounts();
            countCommentEmotion(commentEmotion, commentEmotionCounts);

            if(isUserEmotionEqualsUser(commentEmotion, uid)){
                userEmotionInfo.setUserClicked(true);
                userEmotionInfo.setUserClickEmotionType(commentEmotion.getEmotionType());
            }
        }

        res.setUserEmotionInfo(userEmotionInfo);
        return res;
    }

    private static boolean isUserEmotionEqualsUser(CommentDto.CommentEmotionDto commentEmotionDto, String uid) {
        if(commentEmotionDto.getUid().equals(uid)){
            return true;
        }


        return false;
    }


    public static CommentResponse ofUserNull(CommentDto comment){
        UserEmotionInfo<CommentEmotionType> userEmotionInfo = UserEmotionInfo.<CommentEmotionType>builder().build();

        CommentResponse res = CommentResponse.builder()
                .expertUid(comment.getExpertUid())
                .expertProfileImage(comment.getExpertProfileImage())
                .content(comment.getContent())
                .userEmotionInfo(userEmotionInfo)
                .expertName(comment.getExpertName()).build();

        // Comment 감정의 갯수 측정 및 유저가 emotion을 클릭했는지 조회한다.
        for(CommentDto.CommentEmotionDto commentEmotion :  comment.getCommentEmotions()){
            Map<CommentEmotionType, Integer> commentEmotionCounts = res.getEmotionCounts();
            countCommentEmotion(commentEmotion, commentEmotionCounts);

        }


        return res;
    }

    private static void countCommentEmotion(CommentDto.CommentEmotionDto commentEmotion, Map<CommentEmotionType, Integer> commentEmotionCounts) {

        Integer prevCount = commentEmotionCounts.get(commentEmotion.getEmotionType());

        commentEmotionCounts.put(commentEmotion.getEmotionType(), prevCount+1);
    }


}
