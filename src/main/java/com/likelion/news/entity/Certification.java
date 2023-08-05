package com.likelion.news.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor()
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "certifications")
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certification_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "expert_info_id")
    private ExpertInfo expertInfo;

    private String certificationUrl;

}
