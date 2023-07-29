package com.likelion.news.dto;

import com.likelion.news.entity.CommentEmotion;
import com.likelion.news.entity.enums.CommentEmotionType;
import lombok.*;

import java.util.List;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Data
public class CommentDto {

    private String expertUid;

    private String expertProfileImage;

    private String expertName;

    private List<CommentEmotionDto> commentEmotions;

    private String content;

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
