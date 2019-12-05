package com.squeaker.entry.domain.repository;

import com.squeaker.entry.domain.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUserId(String userId);
    User findByUserId(String userId);
    User findByUuid(Integer uuid);
}
