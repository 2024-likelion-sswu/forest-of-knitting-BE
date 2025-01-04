package com.example.knitforest.dto.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavedDesignResponse {

    private String imgUrl;  // 도안 이미지 URL
    private Integer time;   // 소요 시간

    public SavedDesignResponse(String imgUrl, Integer time) {
        this.imgUrl = imgUrl;
        this.time = time;
    }
}
