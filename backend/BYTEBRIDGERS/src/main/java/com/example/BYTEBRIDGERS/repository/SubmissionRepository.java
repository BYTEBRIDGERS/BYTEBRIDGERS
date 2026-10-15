package com.example.BYTEBRIDGERS.repository;

import com.example.BYTEBRIDGERS.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubmissionRepository
        extends JpaRepository<Submission, Integer> {

    List<Submission> findByUserId(Integer userId);

    long countByUserId(Integer userId);

    long countByUserIdAndVerdict(
            Integer userId,
            String verdict
    );

    @Query("""
            SELECT COUNT(DISTINCT s.problemId)
            FROM Submission s
            WHERE s.userId = :userId
            AND s.verdict = 'ACCEPTED'
            """)
    long countProblemsSolved(
            @Param("userId") Integer userId
    );
}