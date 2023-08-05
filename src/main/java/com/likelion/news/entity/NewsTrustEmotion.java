package com.likelion.news.entity;

import com.likelion.news.entity.enums.NewsTrustEmotionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "news_trust_emotions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class NewsTrustEmotion implements UserEmotion{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsTrustEmotionId;
    @ManyToOne
    @JoinColumn(name = "refined_news_id")
    private RefinedNews refinedNews;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private NewsTrustEmotionType trustEmotionType;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsTrustEmotion that = (NewsTrustEmotion) o;
        return Objects.equals(newsTrustEmotionId, that.newsTrustEmotionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newsTrustEmotionId);
    }
}

