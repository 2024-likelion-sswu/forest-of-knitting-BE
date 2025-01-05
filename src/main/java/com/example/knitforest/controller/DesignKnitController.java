package com.example.knitforest.controller;

import com.example.knitforest.Jwt.ApiResponse;
import com.example.knitforest.Jwt.CustomUserDetails;
import com.example.knitforest.dto.Request.DesignKnitRequest;
import com.example.knitforest.dto.Response.SavedDesignResponse;
import com.example.knitforest.service.DesignKnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/designknit")
@RequiredArgsConstructor
public class DesignKnitController {

    private final DesignKnitService designKnitService;

    @PostMapping("/update")
    public ResponseEntity<?> updateSavedDesign(@ModelAttribute DesignKnitRequest request,
                                               @AuthenticationPrincipal CustomUserDetails user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "사용자가 인증되지 않았습니다.", null));
        }

        try {
            designKnitService.manageKnitRecord(request, user.getUsername());
            return ResponseEntity.ok(new ApiResponse(true, "저장된 도안 업데이트 성공", null));
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, ex.getMessage(), null));
        }
    }



    @GetMapping("/saved")
    public List<SavedDesignResponse> getSavedDesigns(@AuthenticationPrincipal CustomUserDetails user) {
        return designKnitService.getAllSavedDesigns(user.getUsername());
    }

    @GetMapping("/completed")
    public ResponseEntity<List<SavedDesignResponse>> getCompletedSavedDesigns(@AuthenticationPrincipal CustomUserDetails user) {
        List<SavedDesignResponse> response = designKnitService.getCompletedSavedDesigns(user.getUsername());
        return ResponseEntity.ok(response);
    }

}
