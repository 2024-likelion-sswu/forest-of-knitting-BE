package com.example.knitforest.Jwt;

import com.example.knitforest.dto.Request.SignUpRequest;
import com.example.knitforest.dto.Request.UserInfo;
import com.example.knitforest.entity.Users;
import com.example.knitforest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Users user = userRepository.findByUserId(userId).orElseThrow(()->new UsernameNotFoundException("해당 유저 없음"));

        UserInfo userInfo = UserInfo.toDto(user);
        return new CustomUserDetails(userInfo);
    }
}
