package com.likelion.news.service;

import com.likelion.news.dto.UserInfoDto;
import com.likelion.news.entity.User;

import com.likelion.news.exception.NotFoundException.NoUserException;
import com.likelion.news.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserInfoDto getUserInfo(String uid){
        User findUser = userRepository.findUserByUid(uid).orElseThrow(NoUserException::new);

        return UserInfoDto.builder()
                .name(findUser.getName())
                .email(findUser.getEmail())
                .nickname(findUser.getNickname())
                .build();
    }

    public void editUserNickname(String uid, String nickname){
        User findUser = userRepository.findUserByUid(uid).orElseThrow(NoUserException::new);
        findUser.setNickname(nickname);
    }

}
