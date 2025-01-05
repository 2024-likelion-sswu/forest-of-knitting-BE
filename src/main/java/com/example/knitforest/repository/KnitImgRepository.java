package com.example.knitforest.repository;

import com.example.knitforest.entity.KnitImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnitImgRepository extends JpaRepository<KnitImg, Long> {
    List<KnitImg> findAllByKnitRecord_Id(Long recordId);
}
