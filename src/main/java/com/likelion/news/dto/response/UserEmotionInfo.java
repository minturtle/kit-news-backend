package com.likelion.news.dto.response;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserEmotionInfo<T>{
    @Builder.Default
    private boolean isUserClicked = false;
    private T userClickEmotionType;

}
