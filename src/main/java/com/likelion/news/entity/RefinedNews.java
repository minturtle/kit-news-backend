package com.likelion.news.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "refined_news")
@Getter
public class RefinedNews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refinedNewsId;

    @ManyToOne
    @JoinColumn(name = "crawled_news_id")
    private CrawledNews crawledNews;

    private String articleSummary;

}