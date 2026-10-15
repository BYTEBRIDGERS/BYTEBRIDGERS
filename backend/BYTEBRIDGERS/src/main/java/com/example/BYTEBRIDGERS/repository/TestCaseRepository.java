package com.example.BYTEBRIDGERS.repository;

import com.example.BYTEBRIDGERS.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestCaseRepository
        extends JpaRepository<TestCase, Integer> {

    List<TestCase> findByProblemId(Integer problemId);
}