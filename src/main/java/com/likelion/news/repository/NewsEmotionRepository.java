package com.likelion.news.repository;


import com.likelion.news.entity.NewsEmotion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsEmotionRepository extends CrudRepository<NewsEmotion, Long> {
}
