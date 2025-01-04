package com.example.knitforest.service;

import com.example.knitforest.dto.Response.SavedDesignResponse;
import com.example.knitforest.entity.*;
import com.example.knitforest.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        // 사용자의 모든 SavedDesign 조회
        List<SavedDesign> savedDesigns = savedDesignRepository.findByUserId(userId);

        // 각 SavedDesign의 KnitRecord를 사용하여 도안 이미지를 조회
        return savedDesigns.stream()
                .map(savedDesign -> {
                    // KnitRecord를 통해 KnitDesignImg 조회
                    KnitDesignImg knitDesignImg = knitDesignImgRepository.findByKnitRecord(savedDesign.getKnitRecord())
                            .orElseThrow(() -> new RuntimeException("도안 이미지가 존재하지 않습니다."));

                    // Response 생성
                    return new SavedDesignResponse(knitDesignImg.getImgUrl(), savedDesign.getTime());
                })
                .collect(Collectors.toList());
    }


    public void manageKnitRecord(Integer time, Long userId, Long knitRecordId, Boolean isCompleted) {
        // 사용자와 뜨개 기록 엔티티를 ID로 조회
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        KnitRecord knitRecord = knitRecordRepository.findById(knitRecordId)
                .orElseThrow(() -> new RuntimeException("뜨개 기록을 찾을 수 없습니다."));


        // SavedDesign을 조회하거나 새로 생성
        SavedDesign savedDesign = savedDesignRepository.findByUserAndKnitRecord(user, knitRecord)
                .orElse(SavedDesign.builder().user(user).knitRecord(knitRecord).time(0).isCompleted(false).build());

        if (isCompleted) {
            // isCompleted가 true일 경우 처리
            savedDesign.setIsCompleted(true); // isCompleted를 true로 변경
        }

        // time을 추가하고 저장
        savedDesign.setTime(savedDesign.getTime() + time); // time에 입력받은 시간 더하기
        savedDesignRepository.save(savedDesign); // 업데이트된 SavedDesign 저장

        AccTime accTime = user.getAccTime(); // Users 클래스에서 AccTime에 접근
        if (accTime == null) {
            accTime = new AccTime();
            accTime.setUser(user);
            accTime.setAccTime(time); // AccTime 초기화
        } else {
            accTime.setAccTime(accTime.getAccTime() + time); // 기존 누적 시간에 추가
        }

        // AccTime 객체 저장
        accTimeRepository.save(accTime);

        // 사용자의 AccTime 업데이트
        user.setAccTime(accTime); // AccTime을 Users에 반영
        userRepository.save(user);
    }
}
