package com.likelion.news.controller;


import com.likelion.news.dto.request.NewsRequest;
import com.likelion.news.dto.response.ApiResponse;
import com.likelion.news.dto.response.NewsResponse;
import com.likelion.news.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/list")
    public ApiResponse<List<NewsResponse>> getNewsList(@RequestBody @Valid NewsRequest reqBody){

        List<NewsResponse> result = newsService.getNewsByCategory(reqBody.getFrom(), reqBody.getSize(), reqBody.getCategory())
                .stream().map(news -> NewsResponse.builder()
                        .link(news.getLink())
                        .title(news.getTitle())
                        .content(news.getContent())
                        .summary(news.getSummary())
                        .articleCategory(news.getArticleCategory())
                        .emotionCounts(news.getEmotionCounts())
                        .build()).toList();


        return ApiResponse.<List<NewsResponse>>builder()
                .data(result)
                .build();
    }

}
