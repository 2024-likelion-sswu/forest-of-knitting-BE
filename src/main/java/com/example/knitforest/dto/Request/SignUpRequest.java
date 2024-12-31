package com.example.knitforest.dto.Request;


import com.example.knitforest.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SignUpRequest {
    private String userId;
    private String nickname;
    private String password;

    public static SignUpRequest toDto(Users user) {
        return new SignUpRequest(
                user.getUserId(),
                user.getNickname(),
                user.getPassword()
        );
    }
}
