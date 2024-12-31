package com.example.knitforest.dto.Request;

import com.example.knitforest.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String userId;
    private String nickname;
    private String password;

    public static UserInfo toDto(Users user) {
        return new UserInfo(
                user.getUserId(),
                user.getNickname(),
                user.getPassword()
        );
    }
}
