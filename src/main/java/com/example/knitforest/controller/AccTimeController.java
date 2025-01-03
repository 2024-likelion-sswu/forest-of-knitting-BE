package com.example.knitforest.controller;

import com.example.knitforest.Jwt.ApiResponse;
import com.example.knitforest.Jwt.CustomUserDetails;
import com.example.knitforest.entity.AccTime;
import com.example.knitforest.service.AccTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccTimeController {
    private final AccTimeService accTimeService;

    @PatchMapping("/updateAccTime")
    public ResponseEntity<?> updateAccTime(@AuthenticationPrincipal CustomUserDetails user, @RequestParam Integer hour, @RequestParam Integer minute) {
        try{
            accTimeService.updateAccTime(user.getUsername(), hour, minute);
            return ResponseEntity.ok(new ApiResponse(true, "시간 업데이트 성공", null));
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, ex.getMessage(), null));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "서버 오류가 발생했습니다.", null));
        }
    }
}
