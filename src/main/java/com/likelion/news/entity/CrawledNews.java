package com.likelion.news.entity;

import com.likelion.news.entity.enums.ArticleCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrawledNews that = (CrawledNews) o;
        return Objects.equals(crawledNewsId, that.crawledNewsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(crawledNewsId);
    }
}