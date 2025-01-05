package com.example.knitforest.dto.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeResponse {
    private Long userId;
    private String nickname;
    private Integer hour;
    private Integer minute;
    private Integer step;
}
