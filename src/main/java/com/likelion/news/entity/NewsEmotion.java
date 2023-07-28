package com.likelion.news.entity;


import com.likelion.news.entity.enums.NewsEmotionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "news_emotions")
public class NewsEmotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsEmotionId;

    @ManyToOne
    @JoinColumn(name = "refined_news_id")
    private RefinedNews refinedNews;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private NewsEmotionType emotionType;

    
}
