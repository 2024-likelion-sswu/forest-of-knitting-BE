package com.example.knitforest.controller;

import com.example.knitforest.dto.Request.DesignKnitRequest;
import com.example.knitforest.dto.Response.SavedDesignResponse;
import com.example.knitforest.service.DesignKnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/designknit")
@RequiredArgsConstructor
public class DesignKnitController {

    private final DesignKnitService designKnitService;

    @PostMapping("/update")
    public ResponseEntity<String> updateKnitRecord(@RequestBody DesignKnitRequest request) {
        try {
            designKnitService.manageKnitRecord(
                    request.getTime(),
                    request.getUserId(),
                    request.getKnitRecordId(),
                    request.getIsCompleted()
            );
            return ResponseEntity.ok("Knit record successfully updated.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/saved/{userId}")
    public List<SavedDesignResponse> getSavedDesigns(@PathVariable("userId") Long userId) {
        return designKnitService.getAllSavedDesigns(userId);
    }

    @GetMapping("/completed/{userId}")
    public ResponseEntity<List<SavedDesignResponse>> getCompletedSavedDesigns(@PathVariable Long userId) {
        List<SavedDesignResponse> response = designKnitService.getCompletedSavedDesigns(userId);
        return ResponseEntity.ok(response);
    }
}
