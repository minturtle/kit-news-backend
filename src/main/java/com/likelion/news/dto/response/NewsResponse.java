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

    private Long newsId;
    private String link;
    private ArticleCategory articleCategory;
    private String title;
    private String summary;
    private String content;



}
