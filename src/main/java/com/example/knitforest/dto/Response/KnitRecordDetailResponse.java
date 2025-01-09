package com.example.knitforest.dto.Response;


import com.example.knitforest.entity.KnitRecord;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class KnitRecordDetailResponse {
    private KnitRecord knitRecord;
    private String knitImgUrl;
    private String designImgUrl;
    private Boolean isBooked;
}
