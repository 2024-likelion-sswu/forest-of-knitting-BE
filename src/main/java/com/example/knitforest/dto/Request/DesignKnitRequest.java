package com.example.knitforest.dto.Request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DesignKnitRequest {
    private Long userId;
    private Long knitRecordId;
    private Integer time;
    private Boolean isCompleted;
}
