package com.example.knitforest.dto.Response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KnitRecordResponse {

    private Long id;
    private String title;

    private Integer time;

    private Integer level;

    private Integer recommendation;

    private String ImgUrl;

    private Boolean isBooked;
}
