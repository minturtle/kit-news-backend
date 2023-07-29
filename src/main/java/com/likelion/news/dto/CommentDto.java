package com.likelion.news.dto;

import com.likelion.news.entity.CommentEmotion;

import java.util.List;

public class CommentDto {

    private String expertUid;

    private String expertProfileImage;

    private String expertName;

    private List<CommentEmotion> commentEmotions;
}
