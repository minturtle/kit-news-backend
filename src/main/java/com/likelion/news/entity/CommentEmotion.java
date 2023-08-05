package com.likelion.news.entity;

import com.likelion.news.entity.enums.CommentEmotionType;
import com.likelion.news.entity.enums.EmotionClass;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "comment_emotions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class CommentEmotion implements UserEmotion{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentEmotionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private CommentEmotionType emotionType;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentEmotion that = (CommentEmotion) o;
        return Objects.equals(commentEmotionId, that.commentEmotionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentEmotionId);
    }
}
