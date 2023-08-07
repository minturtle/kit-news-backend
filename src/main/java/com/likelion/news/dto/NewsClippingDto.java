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

    private String newsTitle;
    private String newsSummary;


    public static NewsClippingDto toDto(NewsClipping entity){
        return NewsClippingDto.builder()
                .clipId(entity.getId())
                .newsId(entity.getClippedNews().getRefinedNewsId())
                .newsTitle(entity.getClippedNews().getCrawledNews().getArticleTitle())
                .newsSummary(entity.getClippedNews().getArticleSummary())
                .uid(entity.getUser().getUid())
                .build();
    }

}
