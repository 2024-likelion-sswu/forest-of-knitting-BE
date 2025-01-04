package com.example.knitforest.repository;

import com.example.knitforest.entity.KnitDesignImg;
import com.example.knitforest.entity.KnitRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KnitDesignImgRepository extends JpaRepository<KnitDesignImg, Long> {
    Optional<KnitDesignImg> findByKnitRecord(KnitRecord knitRecord);
}
