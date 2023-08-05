package com.likelion.news.controller;


import com.likelion.news.dto.CommentDto;
import com.likelion.news.dto.NewsEmotionDto;
import com.likelion.news.dto.NewsTrustEmotionDto;
import com.likelion.news.entity.enums.CommentEmotionType;
import com.likelion.news.entity.enums.EmotionClass;
import com.likelion.news.dto.response.ApiResponse;
import com.likelion.news.dto.response.CommentResponse;
import com.likelion.news.dto.response.NewsEmotionResponse;
import com.likelion.news.dto.response.NewsResponse;
import com.likelion.news.entity.enums.ArticleCategory;
import com.likelion.news.exception.ClientException;
import com.likelion.news.exception.ExceptionMessages;
import com.likelion.news.service.NewsService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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


    @GetMapping("/comment")
    public ApiResponse<List<CommentResponse>> getCommentsByNewsId(@RequestParam @NotNull Long newsId){
        Optional<String> uid = getUid();

        // JWT로 유저가 조회되지 않는다면, UserEmotion 정보를 NULL로 설정해 리턴
        if(uid.isEmpty()){
            List<CommentDto> commentDtoList = newsService.getCommentByNewsId(newsId);

            return ApiResponse.<List<CommentResponse>>builder()
                    .data(commentDtoList.stream().map(CommentResponse::ofUserNull).toList())
                    .build();
        }

        List<CommentDto> commentDtoList = newsService.getCommentByNewsId(newsId);


        return ApiResponse.<List<CommentResponse>>builder()
                .data(commentDtoList.stream().map(c->CommentResponse.of(c, uid.get())).toList())
                .build();

    }


    @GetMapping("/emotions")
    public ApiResponse<NewsEmotionResponse> getNewsEmotionsByNews(@RequestParam @NotNull Long newsId){
        Optional<String> uid = getUid();


        List<NewsEmotionDto> emotionDtos =  newsService.getEmotionsByNews(newsId);
        List<NewsTrustEmotionDto> trustEmotionDto = newsService.getNewsTrustEmotionByNews(newsId);

        return  ApiResponse.<NewsEmotionResponse>builder()
                .data(NewsEmotionResponse.of(newsId, emotionDtos, trustEmotionDto, uid))
                .build();
    }


    @PostMapping("/emotion/news/{newsId}/{emotionClass}/{emotionType}")
    public void emotionClicked(@PathVariable Long newsId, @PathVariable EmotionClass emotionClass, @PathVariable String emotionType){
        final Optional<String> uid = getUid();


        if(uid.isEmpty()){
            throw new ClientException(ExceptionMessages.LOGIN_NEED.getMessage());
        }

        newsService.saveUserEmotion(uid.get(), newsId, emotionClass, emotionType);

    }

    @PostMapping("/emotion/comment/{commentId}/{emotionType}")
    public void commentEmotionClicked(@PathVariable Long commentId, CommentEmotionType emotionType){
        final Optional<String> uid = getUid();


        if(uid.isEmpty()){
            throw new ClientException(ExceptionMessages.LOGIN_NEED.getMessage());
        }

        newsService.saveCommentEmotion(uid.get(), commentId, emotionType);
    }



    /**
     * @author minseok kim
     * @description SecurityContextHolder에서 uid를 가져오는 메서드
     * @param
     * @return SecurityContextHolder에서 조회된 uid
     */
    private Optional<String> getUid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.isAuthenticated()){
            return Optional.of((String)authentication.getPrincipal());
        }

        return Optional.empty();
    }
}
