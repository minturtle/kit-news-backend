package com.likelion.news.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crawled_news_id")
    private CrawledNews crawledNews;

    private String articleSummary;

    @OneToMany(mappedBy = "refinedNews", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<NewsEmotion> emotions = new ArrayList<>();



    @OneToMany(mappedBy = "refinedNews", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

}