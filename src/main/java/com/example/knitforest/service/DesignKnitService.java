package com.example.knitforest.service;

import com.example.knitforest.Jwt.CustomUserDetails;
import com.example.knitforest.dto.Request.DesignKnitRequest;
import com.example.knitforest.dto.Request.RecordRequest;
import com.example.knitforest.dto.Response.SavedDesignResponse;
import com.example.knitforest.entity.*;
import com.example.knitforest.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DesignKnitService {

    private final UserRepository userRepository;
    private final KnitRecordRepository knitRecordRepository;
    private final SavedDesignRepository savedDesignRepository;
    private final AccTimeRepository accTimeRepository;
    private final KnitDesignImgRepository knitDesignImgRepository;


    @Transactional
    public List<SavedDesignResponse> getAllSavedDesigns(String username) {

        Users user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));
        List<SavedDesign> savedDesigns = savedDesignRepository.findByUserIdAndIsCompletedFalse(user.getId());

        return savedDesigns.stream()
                .map(savedDesign -> {
                    KnitDesignImg designImg = knitDesignImgRepository.findByKnitRecord(savedDesign.getKnitRecord())
                            .orElseThrow(() -> new RuntimeException("도안 이미지를 찾을 수 없습니다."));

                    return SavedDesignResponse.builder()
                            .imgUrl(designImg.getImgUrl())
                            .time(savedDesign.getTime())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 완료한 도안 조회0
    @Transactional
    public List<SavedDesignResponse> getCompletedSavedDesigns(String username) {
        // 사용자 정보가 이미 @AuthenticationPrincipal을 통해 제공되므로 user 객체를 그대로 사용

        Users user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));

        List<SavedDesign> completedDesigns = savedDesignRepository.findByUserIdAndIsCompletedTrue(user.getId());

        System.out.println("Completed Designs Retrieved: " + completedDesigns);

        if (completedDesigns.isEmpty()) {
            return Collections.emptyList();
        }

        return completedDesigns.stream()
                .map(savedDesign -> {
                    KnitDesignImg designImg = knitDesignImgRepository.findByKnitRecord(savedDesign.getKnitRecord())
                            .orElseThrow(() -> new RuntimeException("도안 이미지를 찾을 수 없습니다."));

                    return SavedDesignResponse.builder()
                            .imgUrl(designImg.getImgUrl())
                            .time(savedDesign.getTime())
                            .build();
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public void manageKnitRecord(DesignKnitRequest request, String id) throws IOException {
        Users user = userRepository.findByUserId(id)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));


        KnitRecord knitRecord = knitRecordRepository.findById(request.getKnitRecordId())
                .orElseThrow(() -> new RuntimeException("뜨개 기록을 찾을 수 없습니다."));



        SavedDesign savedDesign = savedDesignRepository.findByUserAndKnitRecord(user, knitRecord)
                .orElse(SavedDesign.builder()
                        .user(user)
                        .knitRecord(knitRecord)
                        .time(0)
                        .isCompleted(false)
                        .build());

        if (request.getIsCompleted()) {
            savedDesign.setIsCompleted(true);
        }

        savedDesign.setTime(savedDesign.getTime() + request.getTime());
        savedDesignRepository.save(savedDesign);

        AccTime accTime = user.getAccTime();
        if (accTime == null) {
            accTime = new AccTime();
            accTime.setUser(user);
            accTime.setAccTime(request.getTime());
        } else {
            accTime.setAccTime(accTime.getAccTime() + request.getTime());
        }

        accTimeRepository.save(accTime);
        user.setAccTime(accTime);
        userRepository.save(user);
    }




}
