package com.example.knitforest.repository;

import com.example.knitforest.entity.KnitDesignImg;
import com.example.knitforest.entity.KnitImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnitDesignImgRepository extends JpaRepository<KnitDesignImg, Long> {
    List<KnitDesignImg> findAllByKnitRecord_Id(Long recordId);
}
