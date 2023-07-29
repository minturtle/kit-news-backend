package com.likelion.news.entity;

import com.likelion.news.entity.enums.CommentEmotionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comment_emotions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class CommentEmotion {
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



}
