package com.example.knitforest.repository;

import com.example.knitforest.entity.KnitRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KnitRecordRepository extends JpaRepository<KnitRecord, Long> {
}
