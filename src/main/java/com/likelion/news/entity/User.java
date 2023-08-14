package com.likelion.news.entity;

import com.likelion.news.entity.enums.LoginType;
import com.likelion.news.entity.enums.UserType;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name;

    private Long kakaoUid;

    private String profileImage;

    private String uid;

    private String email;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @CreationTimestamp
    @Builder.Default
    private LocalDateTime createdTime = LocalDateTime.now();


    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    @Builder.Default
    private List<NewsClipping> newsClippingList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
