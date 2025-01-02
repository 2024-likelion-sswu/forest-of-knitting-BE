package com.example.knitforest.service;

import com.example.knitforest.entity.KnitDesignImg;
import com.example.knitforest.entity.KnitImg;
import com.example.knitforest.entity.KnitRecord;
import com.example.knitforest.repository.KnitDesignImgRepository;
import com.example.knitforest.repository.KnitImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImgService {
    private final S3Service s3Service;
    private final KnitImgRepository knitImgRepository;
    private final KnitDesignImgRepository knitDesignImgRepository;

    public void uploadRecordImages(List<MultipartFile> kImgs, List<MultipartFile>kdImgs, KnitRecord record) throws IOException {
        for(MultipartFile img: kImgs){
            String url = s3Service.upload(img,"knitImg");
            KnitImg knitImg = new KnitImg();
            knitImg.setKnitRecord(record);
            knitImg.setImgUrl(url);
            knitImgRepository.save(knitImg);
        }
        for(MultipartFile img: kdImgs){
            String url = s3Service.upload(img,"knitDesignImg");
            KnitDesignImg knitDesignImg = new KnitDesignImg();
            knitDesignImg.setKnitRecord(record);
            knitDesignImg.setImgUrl(url);
            knitDesignImgRepository.save(knitDesignImg);
        }

    }


}
