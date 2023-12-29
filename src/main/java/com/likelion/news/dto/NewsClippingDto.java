package com.likelion.news.dto;


import com.likelion.news.entity.NewsClipping;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewsClippingDto {

    private Long clipId;
    private Long newsId;
    private String uid;

    private String title;
    private String summary;


    public static NewsClippingDto toDto(NewsClipping entity){
        return NewsClippingDto.builder()
                .clipId(entity.getNewsClippingId())
                .newsId(entity.getClippedNews().getRefinedNewsId())
                .title(entity.getClippedNews().getCrawledNews().getArticleTitle())
                .summary(entity.getClippedNews().getArticleSummary())
                .uid(entity.getUser().getUid())
                .build();
    }

}
