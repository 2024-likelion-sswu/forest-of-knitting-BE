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
        knitRecord.setRecommendation(0);
        knitRecordRepository.save(knitRecord);

        imgService.uploadRecordImages(recordRequest.getKnitImages(), recordRequest.getDesignImages(), knitRecord);


    }
    public List<KnitRecordResponse> getKnitRecordsByPage(String userId, int page) {
        PageRequest pageable = PageRequest.of(page, 10);

        Page<KnitRecord> knitRecords = knitRecordRepository.findByIsPostedTrueOrderByRecommendationDesc(pageable);
        List<KnitRecord> saved = savedDesignRepository.findKnitRecordsByUserId(userId);

        return knitRecords.stream()
                .map(record -> {
                    KnitRecordResponse response = new KnitRecordResponse();
                    response.setId(record.getId());
                    response.setTitle(record.getTitle());
                    response.setTime(record.getTime());
                    response.setLevel(record.getLevel());
                    response.setRecommendation(record.getRecommendation());
                    response.setIsBooked(saved.contains(record));

                    // 이미지 URL 조회
                    List<KnitImg> images = knitImgRepository.findAllByKnitRecord_Id(record.getId());
                    response.setImgUrl(images.isEmpty() ? null : images.get(0).getImgUrl());

                    return response;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void recommendation(Long knitRecordId) {
        KnitRecord record = knitRecordRepository.findById(knitRecordId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 기록입니다."));
        record.setRecommendation();
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

        return response;
    }
}
