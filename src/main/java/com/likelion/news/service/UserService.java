package com.likelion.news.service;

import com.likelion.news.dto.UserInfoDto;
import com.likelion.news.entity.ExpertInfo;
import com.likelion.news.entity.User;

import com.likelion.news.entity.enums.ExpertState;
import com.likelion.news.exception.NotFoundException.NoUserException;
import com.likelion.news.repository.ExpertInfoRepository;
import com.likelion.news.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final ExpertInfoRepository expertInfoRepository;

    public UserInfoDto getUserInfo(String uid){
        User findUser = userRepository.findUserByUid(uid).orElseThrow(NoUserException::new);
        Optional<ExpertInfo> isExpert = expertInfoRepository.findByUserUid(uid);
        ExpertState expertState = ExpertState.NONE;
        if(isExpert.isPresent()){
            expertState = isExpert.get().getState();
        }

        return UserInfoDto.builder()
                .name(findUser.getName())
                .email(findUser.getEmail())
                .nickname(findUser.getNickname())
                .expertState(expertState)
                .build();
    }

    public void editUserNickname(String uid, String nickname){
        User findUser = userRepository.findUserByUid(uid).orElseThrow(NoUserException::new);
        findUser.setNickname(nickname);
    }

}
