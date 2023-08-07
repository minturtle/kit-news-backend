package com.likelion.news.service;


import com.likelion.news.entity.*;
import com.likelion.news.entity.enums.CommentEmotionType;
import com.likelion.news.entity.enums.EmotionClass;
import com.likelion.news.entity.enums.NewsEmotionType;
import com.likelion.news.entity.enums.NewsTrustEmotionType;
import com.likelion.news.exception.ExceptionMessages;
import com.likelion.news.exception.ForbiddenException;
import com.likelion.news.repository.CommentEmotionRepository;
import com.likelion.news.repository.NewsEmotionRepository;
import com.likelion.news.repository.NewsTrustEmotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserEmotionService {

    private final NewsEmotionRepository newsEmotionRepository;
    private final NewsTrustEmotionRepository newsTrustEmotionRepository;
    private final CommentEmotionRepository commentEmotionRepository;

    public void saveEmotion(EmotionClass emotionClass, String emotionType, User user, RefinedNews news) {
        if(emotionClass.equals(EmotionClass.NEWS_EMOTION)){
            NewsEmotion newsEmotion = NewsEmotion.builder()
                    .emotionType(NewsEmotionType.valueOf(emotionType))
                    .user(user)
                    .refinedNews(news)
                    .build();

            newsEmotionRepository.save(newsEmotion);

        } else if(emotionClass.equals(EmotionClass.NEWS_TRUST_EMOTION)){
            NewsTrustEmotion newsEmotion = NewsTrustEmotion.builder()
                    .user(user)
                    .refinedNews(news)
                    .trustEmotionType(NewsTrustEmotionType.valueOf(emotionType))
                    .build();

            newsTrustEmotionRepository.save(newsEmotion);
    }

    }

    public void saveCommentEmotion(CommentEmotionType emotionType, User user, Comment comment){

        CommentEmotion userEmotion = CommentEmotion.builder()
                .emotionType(emotionType)
                .user(user)
                .comment(comment)
                .build();

        commentEmotionRepository.save(userEmotion);


    }

    public void deleteCommentEmotion(User user, Comment comment) {
        final Optional<CommentEmotion> commentOptional = commentEmotionRepository.findByCommentAndUser(comment, user);
        if(commentOptional.isEmpty()){
            return;
        }


        CommentEmotion commentEmotion = commentOptional.get();

        if(!commentEmotion.getUser().equals(user)){
            throw new ForbiddenException(ExceptionMessages.FORBIDDEN.getMessage());
        }

        commentEmotionRepository.delete(commentEmotion);

    }

    public void deleteEmotion(User user, RefinedNews news, EmotionClass emotionClass) {
        if(emotionClass.equals(EmotionClass.NEWS_EMOTION)){
            final Optional<NewsEmotion> newsEmotionOptional = newsEmotionRepository.findByRefinedNewsAndUser(news, user);

            if(newsEmotionOptional.isEmpty()){
                return;
            }

            NewsEmotion newsEmotion = newsEmotionOptional.get();

            if(!newsEmotion.getUser().equals(user)){
                throw new ForbiddenException(ExceptionMessages.FORBIDDEN.getMessage());
            }

            newsEmotionRepository.delete(newsEmotion);

            return;
        }

        final Optional<NewsTrustEmotion> newsTrustEmotionOptional = newsTrustEmotionRepository.findByRefinedNewsAndUser(news, user);

        if(newsTrustEmotionOptional.isEmpty()){
            return;
        }


        final NewsTrustEmotion newsEmotion = newsTrustEmotionOptional.get();
        if(!newsEmotion.getUser().equals(user)){
            throw new ForbiddenException(ExceptionMessages.FORBIDDEN.getMessage());
        }

        newsTrustEmotionRepository.delete(newsEmotion);


    }
}
