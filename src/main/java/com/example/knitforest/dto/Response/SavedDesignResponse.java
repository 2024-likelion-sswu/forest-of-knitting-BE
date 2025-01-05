package com.example.knitforest.dto.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SavedDesignResponse {

    private String imgUrl;
    private Integer time;

    public SavedDesignResponse(String imgUrl, Integer time) {
        this.imgUrl = imgUrl;
        this.time = time;
    }


}
