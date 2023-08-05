package com.likelion.news.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpertInfo that = (ExpertInfo) o;
        return Objects.equals(expertInfoId, that.expertInfoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expertInfoId);
    }
}
