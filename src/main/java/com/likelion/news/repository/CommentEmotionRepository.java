package com.likelion.news.repository;

import com.likelion.news.entity.CommentEmotion;
import com.likelion.news.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentEmotionRepository extends CrudRepository<CommentEmotion, Long> {

    Optional<CommentEmotion> findByUser(User user);
}
