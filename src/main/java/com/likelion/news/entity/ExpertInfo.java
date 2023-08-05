package com.likelion.news.entity;

import com.likelion.news.entity.enums.ExpertState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "expert_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ExpertInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expertInfoId;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    private String job;
    private String company;
    private String businessType;
    private String education;

    @Enumerated(EnumType.STRING)
    private ExpertState expertState;

    
}
