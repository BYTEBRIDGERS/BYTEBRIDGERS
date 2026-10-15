package com.example.BYTEBRIDGERS.service;

import com.example.BYTEBRIDGERS.dto.DashboardResponse;
import com.example.BYTEBRIDGERS.exception.UnauthorizedAccessException;
import com.example.BYTEBRIDGERS.repository.SubmissionRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final SubmissionRepository submissionRepository;

    public DashboardService(
            SubmissionRepository submissionRepository) {

        this.submissionRepository = submissionRepository;
    }

    public DashboardResponse getDashboard(
            Integer userId,
            Integer authenticatedUserId) {
        
        if (!userId.equals(authenticatedUserId)) {
            throw new UnauthorizedAccessException(
                    "You are not allowed to access this dashboard"
            );
        }
        long totalSubmissions =
                submissionRepository.countByUserId(userId);

        long acceptedSubmissions =
                submissionRepository.countByUserIdAndVerdict(
                        userId,
                        "ACCEPTED"
                );

        long wrongAnswers =
                submissionRepository.countByUserIdAndVerdict(
                        userId,
                        "WRONG_ANSWER"
                );

        long problemsSolved =
                submissionRepository.countProblemsSolved(userId);

        return new DashboardResponse(
                userId,
                totalSubmissions,
                acceptedSubmissions,
                wrongAnswers,
                problemsSolved
        );
    }
}