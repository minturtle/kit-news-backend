package com.likelion.news.entity;

import com.likelion.news.entity.enums.CommentEmotionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "comment_emotions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CommentEmotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentEmotionId;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private CommentEmotionType emotionType;

}
