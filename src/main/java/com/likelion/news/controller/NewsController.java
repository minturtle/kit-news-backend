package com.likelion.news.controller;


import com.likelion.news.dto.*;
import com.likelion.news.entity.enums.CommentEmotionType;
import com.likelion.news.entity.enums.EmotionClass;
import com.likelion.news.dto.response.ApiResponse;
import com.likelion.news.dto.response.CommentResponse;
import com.likelion.news.dto.response.NewsEmotionResponse;
import com.likelion.news.dto.response.NewsResponse;
import com.likelion.news.entity.enums.ArticleCategory;
import com.likelion.news.exception.ClientException;
import com.likelion.news.exception.ExceptionMessages;
import com.likelion.news.service.ExpertService;
import com.likelion.news.service.NewsClippingService;
import com.likelion.news.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/news")
@Tag(name = "뉴스 관련 API", description = "뉴스 관련 API입니다. 뉴스의 id는 newsId, 댓글 감정표현, 뉴스 감정표현은 감정표현, 뉴스의 신뢰표현은 신뢰표현이라고 하고 설명하겠습니다.")
public class NewsController {

    private final NewsService newsService;
    private final NewsClippingService newsClippingService;
    private final ExpertService expertService;


    @Operation(
            summary = "뉴스 제목 검색 API",
            description = "뉴스를 제목으로 검색하는 API입니다. 모든 카테고리에 대해 검색되어 결과가 나타납니다."
    )
    @GetMapping("/search/title/{title}")
    public ApiResponse<List<NewsResponse>> findNewsByTitle(@PathVariable String title){
        final List<NewsResponse> newsResponses = newsService.getNewsByTitle(title)
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
                .data(newsResponses)
                .build();
    }


    @Operation(
            summary = "뉴스 리스트 조회 API",
            description = "요약까지 완료된 뉴스 리스트를 가져오는 API입니다. 뉴스의 카테고리 별로 검색이 가능하며, from, size의 디폴트 값은 각각 0, 10입니다.")
    @GetMapping("/list/{articleCategory}")
    public ApiResponse<List<NewsResponse>> getNewsList(
            @RequestParam(required = false, defaultValue = "0")  @Positive Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size,
            @PathVariable ArticleCategory articleCategory
    ){

        List<NewsResponse> result = newsService.getNewsByCategory(from, size, articleCategory)
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



    @Operation(
            summary = "뉴스의 전문가 댓글을 조회하는 API",
            description = "전문가 댓글을 조회하는 API입니다. news의 ID값을 필요로하며, 이에 해당하는 댓글을 조회합니다." +
                    "또 JWT로 사용자 인증에 성공시 사용자가 전문가 댓글에 감정표현(좋아요 등..)을 눌렀는지도 알 수 있습니다.")
    @GetMapping("/{newsId}/comment")
    public ApiResponse<List<CommentResponse>> getCommentsByNewsId(@PathVariable Long newsId){
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

    @Operation(
            summary = "뉴스의 전문가 댓글을 작성하는 API",
            description = "전문가 댓글을 작성하는 API입니다. news의 ID값을 필요로 합니다." +
                    "사용자가 전문가 or 어드민이어야 합니다. ")
    @PostMapping(value="/{newsId}/comment")
    public ResponseEntity<Void> writeComment(ExpertCommentDto comment,
                                             @PathVariable Long newsId){
        String uid = getUid().get();
        expertService.writeComment(uid, newsId, comment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "뉴스의 전문가 댓글을 수정하는 API",
            description = "전문가 댓글을 수정하는 API입니다. news의 ID값과 댓글의 Id값이 필요합니다." +
                    "사용자가 전문가 or 어드민이어야 하고, 본인이 작성한 댓글이어야합니다. ")
    @PatchMapping(value="/{newsId}/comment/{commentId}")
    public ResponseEntity<Void> updateComment(ExpertCommentDto comment,
                                              @PathVariable Long newsId,
                                              @PathVariable Long commentId){
        String uid = getUid().get();
        expertService.updateComment(uid, newsId, commentId, comment);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "뉴스의 전문가 댓글을 삭제하는 API",
            description = "전문가 댓글을 삭제하는 API입니다. news의 ID값과 댓글의 Id값이 필요합니다." +
                    "사용자가 전문가 or 어드민이어야 하고, 본인이 작성한 댓글이어야합니다. ")
    @DeleteMapping(value="/{newsId}/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long newsId,
                                              @PathVariable Long commentId){
        String uid = getUid().get();
        expertService.deleteComment(uid, newsId, commentId);
        return  ResponseEntity.ok().build();
    }



    @Operation(
            summary = "뉴스의 감정표현 갯수, 신뢰표현 갯수 조회 API",
            description = "news의 감정표현, 신뢰표현 갯수를 조회하는 API로, JWT로 사용자 인증시 사용자가 뉴스에 대해 감정표현, 신뢰표현을 눌렀는지 알 수 있습니다."
    )
    @GetMapping("/{newsId}/emotions")
    public ApiResponse<NewsEmotionResponse> getNewsEmotionsByNews(@PathVariable Long newsId){
        Optional<String> uid = getUid();


        List<NewsTrustEmotionDto> trustEmotionDto = newsService.getNewsTrustEmotionByNews(newsId);
        List<NewsEmotionDto> emotionDtos =  newsService.getEmotionsByNews(newsId);


        return  ApiResponse.<NewsEmotionResponse>builder()
                .data(NewsEmotionResponse.of(newsId, emotionDtos, trustEmotionDto, uid))
                .build();
    }



    @Operation(
            summary = "뉴스 스크랩 목록 조회 API",
            description = "뉴스 스크랩 목록 조회 API입니다. 로그인이 반드시 되어야하고(JWT 토큰 필요), JWT로 사용자 인증 성공시 사용자가 스크랩한 뉴스의 정보들을 가져옵니다.")
    @GetMapping("/clip")
    public ApiResponse<List<NewsClippingDto>> getNewsClipping(){
        Optional<String> uid = getUid();


        if(uid.isEmpty()){
            throw new ClientException(ExceptionMessages.LOGIN_NEED.getMessage());
        }

        List<NewsClippingDto> newsClipList = newsClippingService.getNewsClipping(uid.get());


        return ApiResponse.<List<NewsClippingDto>>builder()
                .data(newsClipList)
                .build();

    }


    @Operation(
            summary = "뉴스 스크랩 API",
            description = "뉴스를 스크랩하는 API입니다. JWT를 통한 로그인이 되어야하고, 로그인 성공시 뉴스를 스크랩합니다."
    )
    @PostMapping("/clip/{newsId}")
    public void newsClipped(@PathVariable Long newsId){
        Optional<String> uid = getUid();


        if(uid.isEmpty()){
            throw new ClientException(ExceptionMessages.LOGIN_NEED.getMessage());
        }


        newsClippingService.addNewsClip(uid.get(), newsId);
    }



    @Operation(
            summary = "사용자 감정,신뢰표현 추가 API",
            description = "사용자가 감정 표현, 신뢰표현을 추가할 때 사용하는 API입니다. emotionClass로 감정표헌을 할지, 신뢰표현을 할지 선택할 수 있으며," +
                    " emotionType에는 어떤 감정 표현, 신뢰표현을 할것인지 String으로 전달 할 수 있습니다. 감정표현, 신뢰표현에 따른 emotionType은 아래와 같습니다.\n" +
                    "NEWS_EMOTION(감정표현) : LIKE, DISLIKE \n"+
                    "NEWS_TRUST_EMOTION(신뢰표현) : TRUSTWORTHY, SUSPICIOUS\n" +
                    "JWT를 통한 로그인이 필수로 수행되어야 합니다."
    )
    @PostMapping("/emotion/news/{newsId}/{emotionClass}/{emotionType}")
    public void emotionClicked(@PathVariable Long newsId, @PathVariable EmotionClass emotionClass, @PathVariable String emotionType){
        final Optional<String> uid = getUid();


        if(uid.isEmpty()){
            throw new ClientException(ExceptionMessages.LOGIN_NEED.getMessage());
        }

        newsService.saveUserEmotion(uid.get(), newsId, emotionClass, emotionType);

    }


    @Operation(
            summary = "사용자 댓글 감정표현 추가 API",
            description = "사용자가 댓글 감정표현을 추가하고자 할때 호출하는 API입니다. JWT를 통한 인증이 가능해야하고, commentId로 어떤 댓글에 감정표현을 했는지 명시해야합니다."
    )
    @PostMapping("/emotion/comment/{commentId}/{emotionType}")
    public void commentEmotionClicked(@PathVariable Long commentId, @PathVariable CommentEmotionType emotionType){
        final Optional<String> uid = getUid();


        if(uid.isEmpty()){
            throw new ClientException(ExceptionMessages.LOGIN_NEED.getMessage());
        }

        newsService.saveCommentEmotion(uid.get(), commentId, emotionType);
    }


    @Operation(
            summary = "유저의 스크랩을 제거하는 API",
            description = "사용자의 스크랩을 제거하는 메서드로, 제거하려는 스크랩의 id값이 주어져야합니다. JWT를 통한 인증이 필요하며," +
                    "JWT 토큰으로 인증된 유저와 스크랩 ID에 해당하는 유저의 정보가 다르다면 스크랩이 삭제되지 않습니다."
    )
    @DeleteMapping("/clip/{clipId}")
    public void deleteClip(@PathVariable Long clipId){
        final Optional<String> uid = getUid();


        if(uid.isEmpty()){
            throw new ClientException(ExceptionMessages.LOGIN_NEED.getMessage());
        }


        newsClippingService.deleteNewsClip(uid.get(), clipId);


    }


    @Operation(
            summary = "감정표현, 신뢰표현 삭제 API",
            description = "사용자의 감정표현, 신뢰표현을 삭제하는 API입니다. emotionClass에 따라 감정표현, 신뢰표현을 선택할 수 있으며, " +
                    "인자로 넘어온 news에 해당하는 JWT로 인증된 유저의 감정표현, 신뢰표현을 삭제할 수 있습니다."
    )
    @DeleteMapping("/emotion/news/{newsId}/{emotionClass}")
    public void removeEmotion(
            @PathVariable Long newsId,
            @PathVariable EmotionClass emotionClass
    ){
        final Optional<String> uid = getUid();


        if(uid.isEmpty()){
            throw new ClientException(ExceptionMessages.LOGIN_NEED.getMessage());
        }


        newsService.deleteEmotion(uid.get(),newsId, emotionClass);
    }



    @Operation(
            summary = "Comment 감정표현 삭제 API",
            description = "사용자가 comment에 클릭한 감정표현을 삭제하는 API입니다. JWT인증이 된 사용자가 CommentId에 해당하는 comment의 댓글을 삭제합니다."
    )
    @DeleteMapping("/emotion/comment/{commentId}")
    public void removeCommentEmotion(@PathVariable Long commentId){
        final Optional<String> uid = getUid();


        if(uid.isEmpty()){
            throw new ClientException(ExceptionMessages.LOGIN_NEED.getMessage());
        }

        newsService.deleteCommentEmotion(uid.get(), commentId);
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
