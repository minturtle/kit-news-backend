package com.likelion.news.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "comments")
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String content;

    @Builder.Default
    private LocalDateTime createdTime = LocalDateTime.now();

    @ManyToOne(cascade = CascadeType.REMOVE)
    private RefinedNews refinedNews;


    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<CommentEmotion> commentEmotions;

}
