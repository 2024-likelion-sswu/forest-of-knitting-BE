package com.example.knitforest.repository;

import com.example.knitforest.entity.KnitDesignImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KnitDesignImgRepository extends JpaRepository<KnitDesignImg, Long> {
}
