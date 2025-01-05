package com.example.knitforest.service;


import com.example.knitforest.Jwt.JwtUtil;
import com.example.knitforest.dto.Request.LoginRequest;
import com.example.knitforest.dto.Request.SignUpRequest;
import com.example.knitforest.dto.Request.UserInfo;
import com.example.knitforest.dto.Response.HomeResponse;
import com.example.knitforest.entity.AccTime;
import com.example.knitforest.entity.Users;
import com.example.knitforest.repository.AccTimeRepository;
import com.example.knitforest.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final AccTimeRepository accTimeRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    private final AccTimeService accTimeService;


    public Boolean isDuplicate(String userId) {
        return userRepository.existsByUserId(userId);
    }

    @Transactional
    public void signUp(SignUpRequest signUpRequest) {
        if(userRepository.existsByUserId(signUpRequest.getUserId())){
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        Users newUser = Users.builder()
                .nickname(signUpRequest.getNickname())
                .userId(signUpRequest.getUserId())
                .password(encoder.encode(signUpRequest.getPassword()))
                .build();
        userRepository.save(newUser);

        accTimeService.createAccTime(newUser);


    }
    @Transactional
    public String login(LoginRequest loginRequest) {
        Users user = userRepository.findByUserId(loginRequest.getUserId()).orElseThrow(()->new UsernameNotFoundException("존재하지 않는 유저입니다."));

        if(!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
        UserInfo userInfo = UserInfo.toDto(user);
        return jwtUtil.createAccessToken(userInfo);

    }

    public HomeResponse home(String userId) {
        Users user = userRepository.findByUserId(userId).orElseThrow(()->new UsernameNotFoundException("존재하지 않는 유저입니다."));
        AccTime accTime = accTimeRepository.findByUserId(user.getId()).orElseThrow(()-> new IllegalArgumentException("해당 유저의 누적시간이 존데하지 않습니다."));
        HomeResponse response = new HomeResponse();
        response.setUserId(user.getId());
        response.setNickname(user.getNickname());
        response.setStep(accTime.getStep());
        response.setHour(accTime.getAccTime()/60);
        response.setMinute(accTime.getAccTime()%60);
        return response;
    }

}
