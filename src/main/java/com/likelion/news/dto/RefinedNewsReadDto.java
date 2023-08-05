package com.likelion.news.dto;

import com.likelion.news.entity.Comment;
import com.likelion.news.entity.NewsEmotion;
import com.likelion.news.entity.RefinedNews;
import com.likelion.news.entity.enums.ArticleCategory;
import com.likelion.news.entity.enums.NewsEmotionType;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
@Builder
public class RefinedNewsReadDto {

    private Long id;
    private String title;
    private String content;
    private String summary;
    private String link;
    private ArticleCategory articleCategory;


    @Builder.Default
    private List<CommentDto> comments = new ArrayList<>();


    public static RefinedNewsReadDto toDto(RefinedNews entity){
        RefinedNewsReadDto dto = RefinedNewsReadDto.builder()
                .id(entity.getRefinedNewsId())
                .title(entity.getCrawledNews().getArticleTitle())
                .content(entity.getCrawledNews().getArticleContent())
                .summary(entity.getArticleSummary())
                .link(entity.getCrawledNews().getArticleLink())
                .articleCategory(entity.getCrawledNews().getArticleCategory())
                .build();

        return dto;


    }
}
