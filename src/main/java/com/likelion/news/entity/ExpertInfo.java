package com.likelion.news.entity;

import com.likelion.news.entity.enums.ExpertState;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "expert_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
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
    private ExpertState state;

    @OneToMany(mappedBy = "expertInfo", cascade = CascadeType.ALL)
    private List<Certification> certifications;

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
