package com.likelion.news.entity;

import com.likelion.news.entity.enums.LoginType;
import com.likelion.news.entity.enums.UserType;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "users")
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name;

    private Long kakaoUid;

    private String profileImage;

    private String uid;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @CreationTimestamp
    @Builder.Default
    private LocalDateTime createdTime = LocalDateTime.now();


}
