package com.example.knitforest.dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotNull(message = "아이디를 입력해주세요")
    private String userId;

    @NotNull(message = "비밀번호를 입력해주세요")
    private String password;
}
