package com.likelion.news.controller;


import com.likelion.news.dto.response.ApiResponse;
import com.likelion.news.dto.response.NewsResponse;
import com.likelion.news.entity.enums.ArticleCategory;
import com.likelion.news.service.NewsService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/list")
    public ApiResponse<List<NewsResponse>> getNewsList(
            @RequestParam @NotNull @Positive Integer from,
            @RequestParam @NotNull @Positive Integer size,
            @RequestParam @NotNull ArticleCategory category
    ){

        List<NewsResponse> result = newsService.getNewsByCategory(from, size, category)
                .stream().map(news ->{
                    List<NewsResponse.CommentResponse> comments = news.getComments().stream().map(NewsResponse.CommentResponse::of).toList();


                    return NewsResponse.builder()
                            .link(news.getLink())
                            .title(news.getTitle())
                            .content(news.getContent())
                            .summary(news.getSummary())
                            .articleCategory(news.getArticleCategory())
                            .emotionCounts(news.getEmotionCounts())
                            .comments(comments)
                            .build();

                }).toList();


        return ApiResponse.<List<NewsResponse>>builder()
                .data(result)
                .build();
    }

}
