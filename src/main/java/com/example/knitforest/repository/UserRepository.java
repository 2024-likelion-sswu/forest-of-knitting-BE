package com.example.knitforest.repository;

import com.example.knitforest.entity.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Boolean existsByUserId(String userId);
    Optional<Users> findByUserId(String userId);
}
