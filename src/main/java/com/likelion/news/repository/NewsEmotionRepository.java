package com.likelion.news.repository;


import com.likelion.news.entity.NewsEmotion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsEmotionRepository extends CrudRepository<NewsEmotion, Long> {


    @Query("SELECT e FROM NewsEmotion e WHERE e.refinedNews.refinedNewsId = :newsId")
    List<NewsEmotion> findAllByNewsId(@Param("newsId") Long newsId);
}
