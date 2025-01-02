package com.example.knitforest.dto.Request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Setter
@Getter

public class RecordRequest {



    private String title;

    private Integer hour;

    private Integer minute;

    private Integer level;

    private Boolean isPosted;

    private Integer recommendation;

    private List<MultipartFile> knitImages;
    private List<MultipartFile> designImages;
}
