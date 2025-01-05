package com.example.knitforest.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DesignKnitRequest {
    private Long knitRecordId;
    private Integer time;
    private Boolean isCompleted;
}
