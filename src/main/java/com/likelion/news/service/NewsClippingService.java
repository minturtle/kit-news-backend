package com.likelion.news.service;


import com.likelion.news.dto.NewsClippingDto;
import com.likelion.news.entity.NewsClipping;
import com.likelion.news.entity.RefinedNews;
import com.likelion.news.entity.User;
import com.likelion.news.exception.ExceptionMessages;
import com.likelion.news.exception.ForbiddenException;
import com.likelion.news.repository.NewsClippingRepository;
import com.likelion.news.repository.RefinedNewsRepository;
import com.likelion.news.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NewsClippingService {


    private final UserRepository userRepository;
    private final RefinedNewsRepository newsRepository;
    private final NewsClippingRepository newsClippingRepository;


    public List<NewsClippingDto> getNewsClipping(String uid){

        User user = userRepository.findUserByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.CANNOT_FIND_USER.getMessage()));


        List<NewsClipping> newsClippingList = newsClippingRepository.findAllByUser(user);

        return newsClippingList.stream().map(NewsClippingDto::toDto).toList();
    }


    public void addNewsClip(String uid, Long newsId){
        User user = userRepository.findUserByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.CANNOT_FIND_USER.getMessage()));


        RefinedNews news = newsRepository.findById(newsId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.CANNOT_FIND_ENTITY.getMessage()));

        NewsClipping newsClipping = NewsClipping.builder()
                .user(user)
                .clippedNews(news)
                .build();

        newsClippingRepository.save(newsClipping);
    }


    public void deleteNewsClip(String uid, Long clipId){
        User user = userRepository.findUserByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.CANNOT_FIND_USER.getMessage()));


        NewsClipping findNewsClip = newsClippingRepository.findById(clipId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.CANNOT_FIND_ENTITY.getMessage()));


        if(!findNewsClip.getUser().equals(user)){
            throw new ForbiddenException(ExceptionMessages.FORBIDDEN.getMessage());
        }


        newsClippingRepository.delete(findNewsClip);
    }


}
