package com.likelion.news.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ExpertRequest {
    String job;
    String company;
    String businessType;
    String education;
}
