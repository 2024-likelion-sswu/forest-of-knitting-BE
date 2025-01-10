package com.example.knitforest.repository;

import com.example.knitforest.entity.KnitRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface KnitRecordRepository extends JpaRepository<KnitRecord, Long> {
    //Page<KnitRecord> findByIsPostedTrueOrderByRecommendationDesc(Pageable pageable);
    List<KnitRecord> findByUserId(Long userId);
}
