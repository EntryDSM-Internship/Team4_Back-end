package com.squeaker.entry.domain.repository;

import com.squeaker.entry.domain.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<User, Integer> {
    User findByUserId(String userId);
    User findByUuidAndUserRefreshToken(int uuid, String refreshToken);
}