package com.likelion.news.repository;

import com.likelion.news.entity.RefinedNews;
import com.likelion.news.entity.enums.ArticleCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RefinedNewsRepository extends CrudRepository<RefinedNews, Long> {


    @Query("SELECT n from RefinedNews n WHERE n.crawledNews.articleCategory = :articleCategory")
    List<RefinedNews> findAllByArticleSummary(@Param("articleCategory") ArticleCategory articleCategory, Pageable pageable);


    @Query("SELECT n FROM RefinedNews n WHERE n.crawledNews.articleTitle LIKE %:title%")
    List<RefinedNews> findAllByTitle(@Param("title")String title);
}
