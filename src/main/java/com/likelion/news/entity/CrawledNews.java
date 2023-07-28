package com.likelion.news.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "crawled_news")
public class CrawledNews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long crawledNewsId;
    private LocalDateTime articleDatetime;
    private String articleCategory;
    private String media;
    private String articleTitle;
    private String articleContent;
    private String articleLink;

}