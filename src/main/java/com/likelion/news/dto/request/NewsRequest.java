package com.likelion.news.dto.request;


import com.likelion.news.entity.enums.ArticleCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class NewsRequest {

    @NotNull
    @Positive
    private int from = 0;

    @NotNull
    @Positive
    private int size = 10;

    @NotNull
    private ArticleCategory category;
}
