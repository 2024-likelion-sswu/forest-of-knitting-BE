package com.example.knitforest.service;

import com.example.knitforest.dto.Response.SavedDesignResponse;
import com.example.knitforest.entity.*;
import com.example.knitforest.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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


    public List<SavedDesignResponse> getAllSavedDesigns(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        List<SavedDesign> savedDesigns = savedDesignRepository.findByUserAndIsCompletedFalse(user);


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

    //완료한 도안 조회
    public List<SavedDesignResponse> getCompletedSavedDesigns(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 저장된 도안 중 isCompleted가 true인 것만 조회
        List<SavedDesign> completedDesigns = savedDesignRepository.findByUserAndIsCompletedTrue(user);

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



    public void manageKnitRecord(Integer time, Long userId, Long knitRecordId, Boolean isCompleted) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        KnitRecord knitRecord = knitRecordRepository.findById(knitRecordId)
                .orElseThrow(() -> new RuntimeException("뜨개 기록을 찾을 수 없습니다."));


        SavedDesign savedDesign = savedDesignRepository.findByUserAndKnitRecord(user, knitRecord)
                .orElse(SavedDesign.builder().user(user).knitRecord(knitRecord).time(0).isCompleted(false).build());

        if (isCompleted) {
            savedDesign.setIsCompleted(true);
        }

        savedDesign.setTime(savedDesign.getTime() + time);
        savedDesignRepository.save(savedDesign);

        AccTime accTime = user.getAccTime();
        if (accTime == null) {
            accTime = new AccTime();
            accTime.setUser(user);
            accTime.setAccTime(time);
        } else {
            accTime.setAccTime(accTime.getAccTime() + time);
        }

        accTimeRepository.save(accTime);


        user.setAccTime(accTime);
        userRepository.save(user);
    }
}
