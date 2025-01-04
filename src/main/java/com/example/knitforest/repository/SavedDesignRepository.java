package com.example.knitforest.repository;

import com.example.knitforest.entity.KnitRecord;
import com.example.knitforest.entity.SavedDesign;
import com.example.knitforest.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedDesignRepository extends JpaRepository<SavedDesign, Long> {
    List<SavedDesign> findByUserIdAndIsCompletedFalse(Long userId);
    List<SavedDesign> findByUserIdAndIsCompletedTrue(Long userId);
    Optional<SavedDesign> findByUserAndKnitRecord(Users user, KnitRecord knitRecord);
}
