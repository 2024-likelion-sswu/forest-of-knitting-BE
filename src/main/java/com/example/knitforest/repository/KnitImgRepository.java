package com.example.knitforest.repository;

import com.example.knitforest.entity.KnitImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KnitImgRepository extends JpaRepository<KnitImg, Long> {
}
