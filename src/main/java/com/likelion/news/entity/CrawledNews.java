package com.likelion.news.entity;

import com.likelion.news.entity.enums.ArticleCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "crawled_news")
@Getter
public class CrawledNews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long crawledNewsId;

    @Builder.Default
    private LocalDateTime articleDatetime = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private ArticleCategory articleCategory;
    private String media;
    private String articleTitle;
    private String articleContent;
    private String articleLink;

    @OneToMany(mappedBy = "crawledNews", cascade = CascadeType.REMOVE)
    private List<RefinedNews> refinedNewsList;

}