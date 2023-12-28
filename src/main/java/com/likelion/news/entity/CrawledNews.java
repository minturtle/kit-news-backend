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
    private String uid;
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


    /**
     * @author minseok kim
     * @description contentSize가 범위 내에 속하는지를 체크하는 메서드
     * @param minimumSize content의 최소 크기
     * @param maximumSize content의 최대 크기
     * @return contentSize가 minimumSize ~ maximumSize에 속한다면 true 리턴
    */
    public boolean contentSizeIsIn(Integer minimumSize, Integer maximumSize) {
        return (minimumSize < articleContent.length()) && (maximumSize > articleContent.length());
    }
}