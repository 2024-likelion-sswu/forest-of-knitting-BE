package com.example.knitforest.repository;

import com.example.knitforest.entity.KnitRecord;
import com.example.knitforest.entity.Recommend;

import com.example.knitforest.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {
    Boolean existsByUser_UserIdAndKnitRecord(String userId, KnitRecord knitRecord);
    @Query("SELECT kr FROM KnitRecord kr ORDER BY (SELECT COUNT(r) FROM Recommend r WHERE r.knitRecord = kr) DESC")
    List<KnitRecord> findAllOrderedByRecommendationCount();
    Integer countRecommendByKnitRecord(KnitRecord knitRecord);
}
