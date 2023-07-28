package com.likelion.news.dto.response;


import com.likelion.news.entity.enums.ArticleCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Data
public class NewsResponse {

    private String link;
    private ArticleCategory articleCategory;
    private String title;
    private String summary;
    private String content;
}
