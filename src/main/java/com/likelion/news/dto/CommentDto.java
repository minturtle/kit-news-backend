package com.likelion.news.dto;

import com.likelion.news.entity.Comment;
import com.likelion.news.entity.CommentEmotion;
import com.likelion.news.entity.User;
import com.likelion.news.entity.enums.CommentEmotionType;
import lombok.*;

import java.util.List;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Data
public class CommentDto {

    private Long id;
    private String expertUid;

    private String expertProfileImage;

    private String expertName;

    private String expertNickname;

    private List<CommentEmotionDto> commentEmotions;

    private String content;

    public static CommentDto toDto(Comment entity){

        User expert = entity.getUser();
        return CommentDto.builder()
                .id(entity.getCommentId())
                .expertUid(expert.getUid())
                .expertProfileImage(expert.getProfileImage())
                .expertName(expert.getName())
                .expertNickname(expert.getNickname())
                .content(entity.getContent())
                .commentEmotions(entity.getCommentEmotions().stream().map(CommentEmotionDto::toDto).toList())
                .build();
    }

    @Builder
    @Getter
    public static class CommentEmotionDto{
        private String uid;
        private CommentEmotionType emotionType;

        public static CommentEmotionDto toDto(CommentEmotion entity){
           return CommentEmotionDto.builder()
                   .uid(entity.getUser().getUid())
                   .emotionType(entity.getEmotionType())
                   .build();
        }
    }
}
