package com.likelion.news.repository;

import com.likelion.news.entity.NewsClipping;
import com.likelion.news.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NewsClippingRepository extends CrudRepository<NewsClipping, Long> {

    List<NewsClipping> findAllByUser(User user);
}
