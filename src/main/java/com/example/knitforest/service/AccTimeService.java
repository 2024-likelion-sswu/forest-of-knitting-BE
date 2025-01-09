package com.example.knitforest.service;

import com.example.knitforest.entity.AccTime;
import com.example.knitforest.entity.Users;
import com.example.knitforest.repository.AccTimeRepository;
import com.example.knitforest.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccTimeService {
    private final AccTimeRepository accTimeRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createAccTime(Users user) {
        AccTime accTime = new AccTime();
        accTime.setUser(user);
        accTime.setAccTime(0);
        accTime.setStep(1);
        accTimeRepository.save(accTime);
    }

    @Transactional
    public void updateAccTime(String userId, Integer hour, Integer minute) {
        Users user = userRepository.findByUserId(userId).orElseThrow(()->new UsernameNotFoundException("존재하지 않는 유저입니다."));
        AccTime accTime = accTimeRepository.findByUserId(user.getId()).orElseThrow(()-> new IllegalArgumentException("해당 유저의 누적시간이 존데하지 않습니다."));
        accTime.setAccTime(accTime.getAccTime() + hour * 60 + minute );
    }
}
