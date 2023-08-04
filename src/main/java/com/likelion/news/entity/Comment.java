package com.likelion.news.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(commentId, comment.commentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId);
    }
}
