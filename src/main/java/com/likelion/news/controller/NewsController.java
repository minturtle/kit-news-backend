package com.likelion.news.controller;


import com.likelion.news.dto.CommentDto;
import com.likelion.news.dto.NewsEmotionDto;
import com.likelion.news.dto.response.ApiResponse;
import com.likelion.news.dto.response.CommentResponse;
import com.likelion.news.dto.response.NewsEmotionResponse;
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
                .stream().map(news -> NewsResponse.builder()
                            .newsId(news.getId())
                            .link(news.getLink())
                            .title(news.getTitle())
                            .content(news.getContent())
                            .summary(news.getSummary())
                            .articleCategory(news.getArticleCategory())
                            .build()
                ).toList();


        return ApiResponse.<List<NewsResponse>>builder()
                .data(result)
                .build();
    }


    //TODO : JWT Token을 사용해 사용자가 Comment에 좋아요를 눌렀는지 확인하는 로직 필요
    @GetMapping("/comment")
    public ApiResponse<List<CommentResponse>> getCommentsByNewsId(@RequestParam @NotNull Long newsId){

        List<CommentDto> commentDtoList = newsService.getCommentByNewsId(newsId);

        return ApiResponse.<List<CommentResponse>>builder()
                .data(commentDtoList.stream().map(CommentResponse::of).toList())
                .build();
    }
    //TODO : JWT Token을 사용해 사용자가 news에 좋아요를 눌렀는지 확인하는 로직 필요
    @GetMapping("/emotions")
    public ApiResponse<NewsEmotionResponse> getNewsEmotionsByNews(@RequestParam @NotNull Long newsId){

        List<NewsEmotionDto> emotionDtos =  newsService.getEmotionsByNews(newsId);


        return  ApiResponse.<NewsEmotionResponse>builder()
                .data(NewsEmotionResponse.of(newsId, emotionDtos))
                .build();
    }


}
