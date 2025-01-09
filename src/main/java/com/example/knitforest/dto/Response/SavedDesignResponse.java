package com.example.knitforest.dto.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SavedDesignResponse {

    private Long knitRecordId;
    private String imgUrl;
    private Integer hour;
    private Integer minute;

    public SavedDesignResponse(Long knitRecordId,String imgUrl, Integer hour, Integer minute) {
        this.knitRecordId=knitRecordId;
        this.imgUrl = imgUrl;
        this.hour = hour;
        this.minute = minute;

    }


}
