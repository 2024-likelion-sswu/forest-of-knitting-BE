package com.example.knitforest.controller;

import com.example.knitforest.Jwt.ApiResponse;
import com.example.knitforest.Jwt.CustomUserDetails;
import com.example.knitforest.dto.Request.RecordRequest;
import com.example.knitforest.dto.Response.KnitRecordDetailResponse;
import com.example.knitforest.dto.Response.KnitRecordResponse;
import com.example.knitforest.service.KnitRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/all")
    public ResponseEntity<?> getRecords(@RequestParam int page) {
        try {
            List<KnitRecordResponse> records = knitRecordService.getKnitRecordsByPage(page);
            return ResponseEntity.ok(new ApiResponse(true, "기록 조회 성공", records));

        } catch (IllegalArgumentException ex) {

            return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage(), null));
        } catch (Exception ex) {

            return ResponseEntity.status(500).body(new ApiResponse(false, "서버 에러가 발생했습니다.", null));
        }
    }

    @PostMapping("/recommend")
    public ResponseEntity<?> recommendRecord(@RequestParam Long recordId) {
        try {
            knitRecordService.recommendation(recordId);
            return ResponseEntity.ok(new ApiResponse(true, "추천 성공", null));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new ApiResponse(false, "서버 에러가 발생했습니다.", null));
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getKnitRecordDetail(@RequestParam Long recordId) {
        try {
            KnitRecordDetailResponse response = knitRecordService.getKnitRecordDetail(recordId);
            return ResponseEntity.ok(new ApiResponse(true, "기록 상세 조회 성공", response));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new ApiResponse(false, "서버 에러가 발생했습니다.", null));
        }
    }

    }
