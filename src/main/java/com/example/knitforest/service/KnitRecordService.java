package com.example.knitforest.service;


import com.example.knitforest.dto.Request.RecordRequest;
import com.example.knitforest.dto.Response.KnitRecordDetailResponse;
import com.example.knitforest.dto.Response.KnitRecordResponse;
import com.example.knitforest.entity.*;
import com.example.knitforest.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KnitRecordService {
    private final KnitRecordRepository knitRecordRepository;
    private final UserRepository userRepository;
    private final ImgService imgService;
    private final KnitImgRepository knitImgRepository;
    private final KnitDesignImgRepository knitDesignImgRepository;
    private final SavedDesignRepository savedDesignRepository;
    private final RecommendRepository recommendRepository;

    @Transactional
    public void createRecord(RecordRequest recordRequest, String id) throws IOException {
        Users user = userRepository.findByUserId(id).orElseThrow(()->new UsernameNotFoundException("존재하지 않는 유저입니다."));
        Integer time=recordRequest.getHour()*60+recordRequest.getMinute();
        KnitRecord knitRecord = new KnitRecord();
        knitRecord.setUser(user);
        knitRecord.setTitle(recordRequest.getTitle());
        knitRecord.setTime(time);
        knitRecord.setLevel(recordRequest.getLevel());
        knitRecord.setIsPosted(recordRequest.getIsPosted());
        knitRecordRepository.save(knitRecord);
        imgService.uploadRecordImages(recordRequest.getKnitImages(), recordRequest.getDesignImages(), knitRecord);


    }
    public List<KnitRecordResponse> getKnitRecordsByPage(String userId, int page) {
        PageRequest pageable = PageRequest.of(page, 10);

        // 추천 수를 기준으로 정렬된 KnitRecord 목록을 가져옵니다.
        List<KnitRecord> knitRecords = recommendRepository.findAllOrderedByRecommendationCount();

        // 페이지 처리를 위해서 전체 목록을 나누는 방법
        int startIndex = pageable.getPageNumber() * pageable.getPageSize();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), knitRecords.size());

        // 페이지에 맞게 데이터를 자르기
        List<KnitRecord> pageOfKnitRecords = knitRecords.subList(startIndex, endIndex);

        // 사용자 저장된 디자인 목록을 가져옵니다.
        List<KnitRecord> saved = savedDesignRepository.findKnitRecordsByUserId(userId);

        return pageOfKnitRecords.stream()
                .map(record -> {
                    KnitRecordResponse response = new KnitRecordResponse();
                    response.setId(record.getId());
                    response.setTitle(record.getTitle());
                    response.setTime(record.getTime());
                    response.setLevel(record.getLevel());
                    response.setIsBooked(saved.contains(record));
                    response.setMyRecommend(recommendRepository.existsByUser_UserIdAndKnitRecord(userId, record));
                    response.setRecommendation(recommendRepository.countRecommendByKnitRecord(record));

                    // 이미지 URL 조회
                    List<KnitImg> images = knitImgRepository.findAllByKnitRecord_Id(record.getId());
                    response.setImgUrl(images.isEmpty() ? null : images.get(0).getImgUrl());

                    return response;
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public void recommendation(String userId, Long knitRecordId) {
        KnitRecord record = knitRecordRepository.findById(knitRecordId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 기록입니다."));
        Users user = userRepository.findByUserId(userId).orElseThrow(()->new UsernameNotFoundException("존재하지 않는 유저입니다."));
        Recommend recommend = new Recommend();
        recommend.setUser(user);
        recommend.setKnitRecord(record);
        recommendRepository.save(recommend);
    }

    public KnitRecordDetailResponse getKnitRecordDetail(String userId, Long knitRecordId) {
        KnitRecord record = knitRecordRepository.findById(knitRecordId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 기록입니다."));
        List<String> knitImages = knitImgRepository.findAllByKnitRecord_Id(knitRecordId).stream().map(KnitImg::getImgUrl).toList();
        List<String> designImages = knitDesignImgRepository.findAllByKnitRecord_Id(knitRecordId).stream().map(KnitDesignImg::getImgUrl).toList();
        List<KnitRecord> saved = savedDesignRepository.findKnitRecordsByUserId(userId);
        KnitRecordDetailResponse response = new KnitRecordDetailResponse();
        response.setKnitRecord(record);
        response.setKnitImgUrl(knitImages.isEmpty() ? null : knitImages.getFirst());
        response.setDesignImgUrl(designImages.isEmpty() ? null : designImages.getFirst());
        response.setIsBooked(saved.contains(record));
        response.setMyRecommend(recommendRepository.existsByUser_UserIdAndKnitRecord(userId, record));
        response.setRecommendation(recommendRepository.countRecommendByKnitRecord(record));

        return response;
    }
}
