package com.likelion.news.repository;

import com.likelion.news.entity.NewsTrustEmotion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewsTrustEmotionRepository extends CrudRepository<NewsTrustEmotion, Long> {


    @Query("SELECT e FROM NewsTrustEmotion e WHERE e.refinedNews.refinedNewsId = :newsId")
    List<NewsTrustEmotion> findByNewsId(@Param("newsId") Long newsId);
}
