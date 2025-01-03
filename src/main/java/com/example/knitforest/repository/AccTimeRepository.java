package com.example.knitforest.repository;

import com.example.knitforest.entity.AccTime;
import com.example.knitforest.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccTimeRepository extends JpaRepository<AccTime, Long> {
    Optional<AccTime> findByUserId(Long userId);
}
