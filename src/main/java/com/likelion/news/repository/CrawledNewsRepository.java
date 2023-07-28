package com.likelion.news.repository;


import com.likelion.news.entity.CrawledNews;
import com.likelion.news.entity.enums.ArticleCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrawledNewsRepository extends CrudRepository<CrawledNews, Long> {

    List<CrawledNews> findAllByArticleCategory(ArticleCategory articleCategories);
}
