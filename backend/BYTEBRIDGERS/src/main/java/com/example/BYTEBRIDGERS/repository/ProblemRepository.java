package com.example.BYTEBRIDGERS.repository;

import com.example.BYTEBRIDGERS.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, Integer> {
}
