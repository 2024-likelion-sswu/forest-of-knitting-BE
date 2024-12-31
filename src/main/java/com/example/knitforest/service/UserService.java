package com.example.knitforest.service;


import com.example.knitforest.Jwt.JwtUtil;
import com.example.knitforest.dto.Request.LoginRequest;
import com.example.knitforest.dto.Request.SignUpRequest;
import com.example.knitforest.dto.Request.UserInfo;
import com.example.knitforest.entity.Users;
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
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;


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

}
