package com.example.BYTEBRIDGERS.repository;

import com.example.BYTEBRIDGERS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository
        extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
