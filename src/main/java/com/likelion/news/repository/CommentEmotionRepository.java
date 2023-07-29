package com.likelion.news.repository;

import com.likelion.news.entity.CommentEmotion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentEmotionRepository extends CrudRepository<CommentEmotion, Long> {
}
