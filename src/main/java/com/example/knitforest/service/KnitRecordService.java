package com.example.knitforest.service;


import com.example.knitforest.dto.Request.RecordRequest;
import com.example.knitforest.entity.KnitRecord;
import com.example.knitforest.entity.Users;
import com.example.knitforest.repository.KnitRecordRepository;
import com.example.knitforest.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class KnitRecordService {
    private final KnitRecordRepository knitRecordRepository;
    private final UserRepository userRepository;
    private final ImgService imgService;

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
}
