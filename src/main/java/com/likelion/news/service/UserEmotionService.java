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

    public void deleteCommentEmotion(String uid, Long commentEmotionId) {
        CommentEmotion commentEmotion = commentEmotionRepository.findById(commentEmotionId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.CANNOT_FIND_ENTITY.getMessage()));

        if(!commentEmotion.getUser().getUid().equals(uid)){
            throw new ForbiddenException(ExceptionMessages.FORBIDDEN.getMessage());
        }

        commentEmotionRepository.delete(commentEmotion);

    }

    public void deleteEmotion(String uid, EmotionClass emotionClass, Long emotionId) {
        if(emotionClass.equals(EmotionClass.NEWS_EMOTION)){
            NewsEmotion newsEmotion = newsEmotionRepository.findById(emotionId)
                    .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.CANNOT_FIND_ENTITY.getMessage()));

            if(!newsEmotion.getUser().getUid().equals(uid)){
                throw new ForbiddenException(ExceptionMessages.FORBIDDEN.getMessage());
            }

            newsEmotionRepository.delete(newsEmotion);
        }

        final NewsTrustEmotion newsEmotion = newsTrustEmotionRepository.findById(emotionId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.CANNOT_FIND_ENTITY.getMessage()));

        if(!newsEmotion.getUser().getUid().equals(uid)){
            throw new ForbiddenException(ExceptionMessages.FORBIDDEN.getMessage());
        }

        newsTrustEmotionRepository.delete(newsEmotion);


    }
}
