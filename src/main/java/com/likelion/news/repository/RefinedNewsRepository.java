package com.likelion.news.repository;

import com.likelion.news.entity.RefinedNews;
import com.likelion.news.entity.enums.ArticleCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RefinedNewsRepository extends CrudRepository<RefinedNews, Long> {

    List<RefinedNews> findAllByArticleSummary(ArticleCategory articleCategory, Pageable pageable);
}
