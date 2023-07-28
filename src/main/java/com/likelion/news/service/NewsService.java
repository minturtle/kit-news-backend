package com.likelion.news.service;


import com.likelion.news.dto.RefinedNewsContentDto;
import com.likelion.news.dto.RefinedNewsReadDto;
import com.likelion.news.entity.CrawledNews;
import com.likelion.news.entity.RefinedNews;
import com.likelion.news.entity.enums.ArticleCategory;
import com.likelion.news.repository.CrawledNewsRepository;
import com.likelion.news.repository.RefinedNewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final CrawledNewsRepository crawledNewsRepository;
    private final RefinedNewsRepository refinedNewsRepository;



    public List<RefinedNewsReadDto> getNewsByCategory(Pageable pageable, ArticleCategory category){
        return List.of();
    }

    /**
    * @methodName getRandomNews
    * @author : Minseok Kim
    * @description articleType에 속한 뉴스중 size개의 뉴스를 뽑아옴
    *
    * @param size 뽑아올 뉴스의 갯수
    * @param articleType 조회할 뉴스의 카테고리
    * @return List<CrawledNews>
     */
    public List<CrawledNews> getRandomNews(Integer size, ArticleCategory articleType){
        List<CrawledNews> newsList = crawledNewsRepository.findAllByArticleCategory(articleType);
        Set<Integer> randomNumbers = getRandomNumbers(newsList.size(), size);
        ArrayList<CrawledNews> result = new ArrayList<>();
        
        randomNumbers.forEach(i->result.add(newsList.get(i)));
        
        return result;
    }
    /**
     * @methodName getRandomNews
     * @author : Minseok Kim
     * @description articleTypes에 속한 뉴스중 size * length(articleTypes)개의 뉴스를 뽑아옴
     *
     * @param size 뽑아올 뉴스의 갯수
     * @param articleTypes 조회할 뉴스의 카테고리 리스트
     * @return List<CrawledNews>
     */
    public List<CrawledNews> getRandomNews(Integer size, List<ArticleCategory> articleTypes){
        ArrayList<CrawledNews> result = new ArrayList<>();

        for(ArticleCategory articleType : articleTypes) {
            List<CrawledNews> randomNewsList = getRandomNews(size, articleType);

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
    public void saveRefinedNewsList(List<RefinedNewsContentDto> dtoList){
        List<RefinedNews> refinedNewsList = dtoList.stream().map(RefinedNewsContentDto::toEntity).toList();

        refinedNewsRepository.saveAll(refinedNewsList);
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
