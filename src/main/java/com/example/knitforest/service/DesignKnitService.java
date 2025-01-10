package com.example.knitforest.service;

import com.example.knitforest.Jwt.CustomUserDetails;
import com.example.knitforest.dto.Request.DesignKnitRequest;
import com.example.knitforest.dto.Request.RecordRequest;
import com.example.knitforest.dto.Response.SavedDesignResponse;
import com.example.knitforest.entity.*;
import com.example.knitforest.repository.*;
import jakarta.persistence.EntityNotFoundException;
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
    private final AccTimeService accTimeService;
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
                            .knitRecordId(savedDesign.getKnitRecord().getId())
                            .imgUrl(designImg.getImgUrl())
                            .hour(savedDesign.getTime()/60)
                            .minute(savedDesign.getTime()%60)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 완료한 도안 조회0
    @Transactional
    public List<SavedDesignResponse> getCompletedSavedDesigns(String username) {

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
                            .knitRecordId(savedDesign.getKnitRecord().getId())
                            .imgUrl(designImg.getImgUrl())
                            .hour(savedDesign.getTime()/60)
                            .minute(savedDesign.getTime()%60)
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

        savedDesign.setTime(savedDesign.getTime() + request.getHour()*60 + request.getMinute());
        savedDesignRepository.save(savedDesign);

        AccTime accTime = accTimeRepository.findByUserId(user.getId()).orElseThrow(()-> new IllegalArgumentException("해당 유저의 누적시간이 존데하지 않습니다."));

        accTime.setAccTime(accTime.getAccTime() + request.getHour()*60 + request.getMinute());
        accTimeRepository.save(accTime);
    }

    @Transactional
    public void cancelBookmark(Long knitRecordId, String username) {
        Users user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));


        KnitRecord knitRecord = knitRecordRepository.findById(knitRecordId)
                .orElseThrow(() -> new EntityNotFoundException("해당 뜨개 기록을 찾을 수 없습니다."));

        SavedDesign savedDesign = savedDesignRepository.findByUserAndKnitRecord(user, knitRecord)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저의 북마크를 찾을 수 없습니다."));


        savedDesignRepository.delete(savedDesign);
    }



    @Transactional
    public Integer getSavedDesignTime(Long id) {
        SavedDesign savedDesign = savedDesignRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 저장된 도안을 찾을 수 없습니다."));

        return savedDesign.getTime();
    }


}
