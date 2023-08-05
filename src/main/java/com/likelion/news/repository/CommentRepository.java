package com.likelion.news.repository;

import com.likelion.news.entity.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Query("SELECT c from Comment c WHERE c.refinedNews.refinedNewsId = :newsId")
    List<Comment> findCommentsByNewsId(@Param("newsId") Long newsId);

}
