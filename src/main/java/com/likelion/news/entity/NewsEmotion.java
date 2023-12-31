package com.likelion.news.entity;


import com.likelion.news.entity.enums.NewsEmotionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "news_emotions")
public class NewsEmotion implements UserEmotion{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsEmotionId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refined_news_id")
    private RefinedNews refinedNews;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private NewsEmotionType emotionType;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsEmotion that = (NewsEmotion) o;
        return Objects.equals(newsEmotionId, that.newsEmotionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newsEmotionId);
    }
}
