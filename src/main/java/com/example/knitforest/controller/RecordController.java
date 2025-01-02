package com.example.knitforest.controller;

import com.example.knitforest.Jwt.ApiResponse;
import com.example.knitforest.Jwt.CustomUserDetails;
import com.example.knitforest.dto.Request.RecordRequest;
import com.example.knitforest.dto.Request.SignUpRequest;
import com.example.knitforest.service.KnitRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/record")
@RequiredArgsConstructor
@RestController
public class RecordController {
    private final KnitRecordService knitRecordService;

    @PostMapping("/create")
    public ResponseEntity<?> createRecord(@ModelAttribute RecordRequest recordRequest, @AuthenticationPrincipal CustomUserDetails user) {
        try {
            knitRecordService.createRecord(recordRequest, user.getUsername());
            return ResponseEntity.ok(new ApiResponse(true, "뜨개 기록 작성 성공", null));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, ex.getMessage(), null));
        }
    }
    }
