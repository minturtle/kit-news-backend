package com.likelion.news.entity;


import com.likelion.news.entity.enums.NewsEmotionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "news_emotions")
public class NewsEmotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsEmotionId;


    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "refined_news_id")
    private RefinedNews refinedNews;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private NewsEmotionType emotionType;

    
}
