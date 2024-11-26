package com.sw.AurudaArticle.service;

import com.sw.AurudaArticle.domain.Grade;
import com.sw.AurudaArticle.domain.User;

import com.sw.AurudaArticle.exception.CustomException;
import com.sw.AurudaArticle.exception.ErrorCode;
import com.sw.AurudaArticle.exception.ErrorMessage;
import com.sw.AurudaArticle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    //유저 찾기
    public User findUser(Long id){
        return userRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND, ErrorMessage.NO_USER));

    }

    //포인트 증가
    @Transactional
    public void upPoint(Long userId){
        User user = findUser(userId);
        user.plusPoint();
        updateGrade(user);

    }
    //0~900 :  찾길이- 1000~1900 : 떠돌이- 2000~3900: 나그네- 4000~9900:  길잡이- 10000~ : 바람지기
    //포인트별로 등급 관리
    @Transactional
    public void updateGrade(User user){
        if(user.getPoint()<1000){
            user.updateGrade(Grade.E);
        } else if (user.getPoint()<2000) {
            user.updateGrade(Grade.D);
        }else if(user.getPoint()<4000) {
            user.updateGrade(Grade.C);
        }else if(user.getPoint()<10000) {
            user.updateGrade(Grade.B);
        } else {
            user.updateGrade(Grade.A);
        }
    }

}
