package com.likelion.news.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.news.dto.response.ApiResponse;
import com.likelion.news.dto.response.CommentResponse;
import com.likelion.news.dto.response.NewsEmotionResponse;
import com.likelion.news.dto.response.NewsResponse;
import com.likelion.news.entity.*;
import com.likelion.news.entity.enums.*;
import com.likelion.news.repository.*;
import org.antlr.v4.runtime.misc.LogManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class NewsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CrawledNewsRepository crawledNewsRepository;

    @Autowired
    private RefinedNewsRepository refinedNewsRepository;

    @Autowired
    private NewsEmotionRepository newsEmotionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentEmotionRepository commentEmotionRepository;


    private User testExpert;

    private RefinedNews refinedNews;

    @Autowired
    private ObjectMapper objectMapper;


    @AfterEach
    void tearDown() {
        crawledNewsRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("News List를 조회할 수 있다.")
    void t1() throws Exception {
        //given
        setUpData();

        NewsResponse expectedRespData = NewsResponse.builder()
                .newsId(refinedNews.getRefinedNewsId())
                .link("http://test-new.org")
                .articleCategory(ArticleCategory.IT_SCIENCE)
                .title("테스트 뉴스 제목입니다.")
                .content("테스트 뉴스 본문입니다.")
                .summary("테스트 뉴스 요약본입니다.")
                .build();

        ApiResponse<List<NewsResponse>> expectedRespBody = ApiResponse.<List<NewsResponse>>builder()
                .data(List.of(expectedRespData))
                .build();
        //when & then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/news/list")
                .param("from", "0")
                .param("size", "10")
                .param("category", ArticleCategory.IT_SCIENCE.name()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedRespBody)));

    }

    @Test
    @DisplayName("뉴스가 조회되지 않을때, 200과 함께 빈 data가 리턴된다.")
    void t2() throws Exception {
        //given
        ApiResponse<List<NewsResponse>> expectedRespBody = ApiResponse.<List<NewsResponse>>builder()
                .data(List.of())
                .build();

        //when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/news/list")
                                .param("from", "0")
                                .param("size", "10")
                                .param("category", ArticleCategory.IT_SCIENCE.name()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedRespBody)));


    }


    @Test
    @DisplayName("newsId에 해당하는 comment를 조회하고, 사용자가 Comment에 감정표현을 눌렀는지를 알 수 있다.")
    void t3() throws Exception {
        //given
        setUpData();

        CommentResponse expectedCommentResp = CommentResponse.builder()
                .expertUid(testExpert.getUid())
                .expertName(testExpert.getName())
                .content("금오공대 ㅇㅈ합니다.")
                .emotionCounts(Map.of(CommentEmotionType.LIKE, 1))
                .build();


        ApiResponse<List<CommentResponse>> expectedRespBody = ApiResponse.<List<CommentResponse>>builder()
                .data(List.of(expectedCommentResp)).build();
        //when & then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/news/comment")
                        .param("newsId", String.valueOf(refinedNews.getRefinedNewsId()))
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedRespBody)));

    }

    @Test
    @DisplayName("News ID를 사용해 뉴스 감정표현의 갯수와 사용자가 감정표현을 눌렀는지를 알 수 있다.")
    void t4() throws Exception {
        //given
        setUpData();

        NewsEmotionResponse expectedEmotionResp = NewsEmotionResponse.builder()
                .build();

        ApiResponse<List<NewsEmotionResponse>> expectedRespBody = ApiResponse.<List<NewsEmotionResponse>>builder()
                .data(List.of(expectedEmotionResp)).build();
        //when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/news/emotions")
                                .param("newsId", String.valueOf(refinedNews.getRefinedNewsId()))
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedRespBody)));


    }


    private void setUpData() {
        testExpert = User.builder()
                .name("testUser1")
                .kakaoUid(21234L)
                .uid("abc")
                .loginType(LoginType.KAKAO)
                .userType(UserType.ROLE_EXPERT)
                .build();
        User testUser2 = User.builder()
                .name("testUser2")
                .kakaoUid(21235L)
                .uid("abcd")
                .loginType(LoginType.KAKAO)
                .userType(UserType.ROLE_USER)
                .build();
        User testUser3 = User.builder()
                .name("testUser3")
                .kakaoUid(21236L)
                .uid("abce")
                .loginType(LoginType.KAKAO)
                .userType(UserType.ROLE_USER)
                .build();

        CrawledNews crawledNews = CrawledNews.builder()
                .articleCategory(ArticleCategory.IT_SCIENCE)
                .media("금오일보")
                .articleLink("http://test-new.org")
                .articleTitle("테스트 뉴스 제목입니다.")
                .articleContent("테스트 뉴스 본문입니다.")
                .build();

        refinedNews = RefinedNews.builder()
                .crawledNews(crawledNews)
                .articleSummary("테스트 뉴스 요약본입니다.")
                .build();

        NewsEmotion newsEmotion1 = NewsEmotion.builder()
                .refinedNews(refinedNews)
                .emotionType(NewsEmotionType.LIKE)
                .user(testExpert)
                .build();
        NewsEmotion newsEmotion2 = NewsEmotion.builder()
                .refinedNews(refinedNews)
                .emotionType(NewsEmotionType.LIKE)
                .user(testUser2)
                .build();
        NewsEmotion newsEmotion3 = NewsEmotion.builder()
                .refinedNews(refinedNews)
                .emotionType(NewsEmotionType.DISLIKE)
                .user(testUser3)
                .build();

        Comment testComment = Comment.builder()
                .refinedNews(refinedNews)
                .user(testExpert)
                .content("금오공대 ㅇㅈ합니다.").build();

        CommentEmotion testCommentEmotion = CommentEmotion.builder()
                .user(testUser2)
                .comment(testComment)
                .emotionType(CommentEmotionType.LIKE)
                .build();

        userRepository.saveAll(List.of(testExpert, testUser2, testUser3));
        crawledNewsRepository.save(crawledNews);
        refinedNewsRepository.save(refinedNews);
        newsEmotionRepository.saveAll(List.of(newsEmotion1, newsEmotion2, newsEmotion3));
        commentRepository.save(testComment);
        commentEmotionRepository.save(testCommentEmotion);
    }



}