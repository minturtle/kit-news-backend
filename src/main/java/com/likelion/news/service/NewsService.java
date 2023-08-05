package com.likelion.news.service;


import com.likelion.news.dto.*;
import com.likelion.news.entity.*;
import com.likelion.news.entity.enums.ArticleCategory;
import com.likelion.news.entity.enums.CommentEmotionType;
import com.likelion.news.entity.enums.EmotionClass;
import com.likelion.news.exception.ClientException;
import com.likelion.news.exception.ExceptionMessages;
import com.likelion.news.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsService {


    private final UserRepository userRepository;
    private final CrawledNewsRepository crawledNewsRepository;
    private final RefinedNewsRepository refinedNewsRepository;
    private final CommentRepository commentRepository;
    private final NewsEmotionRepository newsEmotionRepository;
    private final Environment environment;
    private final NewsTrustEmotionRepository newsTrustEmotionRepository;
    private final UserEmotionService userEmotionService;

    public List<RefinedNewsReadDto> getNewsByCategory(int from, int size, ArticleCategory category){
        List<RefinedNews> findNewsList = refinedNewsRepository.findAllByArticleSummary(category, PageRequest.of(from / size, size, Sort.by("refinedNewsId").descending()));

        return findNewsList.stream().map(RefinedNewsReadDto::toDto).toList();

    }

    /**
    * @methodName getRandomNews
    * @author : Minseok Kim
    * @description articleType에 속한 뉴스중 size개의 뉴스를 뽑아옴. 해당하는 뉴스가 없으면 빈 리스트가 리턴됨.
    *
    * @param size 뽑아올 뉴스의 갯수
    * @param articleType 조회할 뉴스의 카테고리
    * @param articleDate : 조회하려는 뉴스의 Date
    * @return List<CrawledNews>
     */
    public List<CrawledNews> getRandomNews(Integer size, ArticleCategory articleType, LocalDate articleDate){
        Integer minimumSize = environment.getProperty("clova.summary.minimum-content-size", Integer.class);
        Integer maximumSize = environment.getProperty("clova.summary.maximum-content-size", Integer.class);


        // 날짜에 해당하는 모든 News를 가져 온다.
        LocalDateTime startDateTime = articleDate.atStartOfDay();
        LocalDateTime endDateTime = articleDate.plusDays(1).atStartOfDay();
        List<CrawledNews> newsList = crawledNewsRepository
                .findAllByArticleCategoryAndArticleDateIs(articleType, startDateTime, endDateTime);

        if(newsList.isEmpty()){
            return List.of();
        }

        Set<CrawledNews> result = new HashSet<>();




        //Random Number을 뽑아온 후, 그에 해당하는 index의 뉴스의 content가 조건을 만족하는지 확인한다.
        // resultSize가 parameter로 받은 size에 만족할 때까지 반복한다.
        loop: while(true){
            Set<Integer> randomNumbers = getRandomNumbers(newsList.size(), size);

            List<CrawledNews> satisfiedNewsList = randomNumbers.stream().map(newsList::get).filter(n -> n.contentSizeIsIn(minimumSize, maximumSize)).toList();

            for(CrawledNews news : satisfiedNewsList){
                if(result.size() >= size){
                    break loop;
                }
                result.add(news);
            }
        }


        
        return new ArrayList<>(result);
    }
    /**
     * @methodName getRandomNews
     * @author : Minseok Kim
     * @description articleTypes에 속한 뉴스중 size * length(articleTypes)개의 뉴스를 뽑아옴
     *
     * @param size 뽑아올 뉴스의 갯수
     * @param articleTypes 조회할 뉴스의 카테고리 리스트
     * @param articleDate 조회하려는 뉴스의 날짜
     * @return List<CrawledNews>
     */
    public List<CrawledNews> getRandomNews(Integer size, List<ArticleCategory> articleTypes, LocalDate articleDate){
        ArrayList<CrawledNews> result = new ArrayList<>();

        for(ArticleCategory articleType : articleTypes) {
            List<CrawledNews> randomNewsList = getRandomNews(size, articleType, articleDate);

            result.addAll(randomNewsList);
        }
        return result;
    }

    /**
    * @methodName saveRefinedNewsList
    * @author : Minseok Kim
    * @description dto로 전달받은 refinedNews를 Entity로 변환해 저장하는 메서드
    *
    * @param  dtoList 저장하려는 refinedNews의 정보가 담긴 DTO List
     */
    @Transactional
    public void saveRefinedNewsList(List<RefinedNewsContentDto> dtoList){
        List<RefinedNews> refinedNewsList = dtoList.stream().map(RefinedNewsContentDto::toEntity).toList();

        refinedNewsRepository.saveAll(refinedNewsList);
    }


    public List<CommentDto> getCommentByNewsId(Long newsId) {
        List<Comment> comments = commentRepository.findCommentsByNewsId(newsId);


        return comments.stream().map(CommentDto::toDto).toList();
    }

    public List<NewsEmotionDto> getEmotionsByNews(Long newsId) {
        List<NewsEmotion> emotions = newsEmotionRepository.findAllByNewsId(newsId);


        return emotions.stream().map(NewsEmotionDto::toDto).toList();
    }

    public List<NewsTrustEmotionDto> getNewsTrustEmotionByNews(Long newsId) {
        return newsTrustEmotionRepository.findByNewsId(newsId).stream().map(NewsTrustEmotionDto::toDto).toList();
    }

    @Transactional
    public void saveUserEmotion(String uid, Long newsId, EmotionClass emotionClass, String emotionType){
        User user = userRepository.findUserByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.CANNOT_FIND_USER.getMessage()));

        RefinedNews news = refinedNewsRepository.findById(newsId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.CANNOT_FIND_ENTITY.getMessage()));
        userEmotionService.saveEmotion(emotionClass, emotionType, user, news);

    }

    @Transactional
    public void saveCommentEmotion(String uid, Long commentId, CommentEmotionType commentEmotionType){
        User user = userRepository.findUserByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.CANNOT_FIND_USER.getMessage()));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.CANNOT_FIND_ENTITY.getMessage()));


        userEmotionService.saveCommentEmotion(commentEmotionType, user, comment);
    }
    /**
     * @methodName getRandomNumbers
     * @author : Minseok Kim
     * @description 중복되지 않은 임의의 숫자를 가져오는 함수
     *
     * @param  maxNumber 가장 최대 나올 수 있는숫자 -1, 즉, 0~maxNumber-1까지의 숫자가 랜덤으로 나온다
     * @param size randomNumber을 가져올 갯수
     */
    private Set<Integer> getRandomNumbers(int maxNumber, int size){
        Set<Integer> randomNumbers = new HashSet<>();

        while (randomNumbers.size() < size){
            int randomNumber = (int)(Math.random() * maxNumber);
            randomNumbers.add(randomNumber);
        }
        return randomNumbers;
    }
}
