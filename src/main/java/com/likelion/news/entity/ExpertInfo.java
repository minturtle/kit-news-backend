package com.likelion.news.entity;

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
    private String job;
    private String company;
    private String businessType;
    private String education;

    
}
