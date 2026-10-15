package com.example.BYTEBRIDGERS.dto;

import java.time.LocalDateTime;

public class SubmissionResponse {

    private Integer submissionId;
    private Integer userId;
    private Integer problemId;
    private String language;
    private String status;
    private String verdict;
    private Integer passedTests;
    private Integer totalTests;
    private Long executionTime;
    private Long memoryUsed;
    private LocalDateTime submittedAt;

    public SubmissionResponse(
            Integer submissionId,
            Integer userId,
            Integer problemId,
            String language,
            String status,
            String verdict,
            Integer passedTests,
            Integer totalTests,
            Long executionTime,
            Long memoryUsed,
            LocalDateTime submittedAt) {

        this.submissionId = submissionId;
        this.userId = userId;
        this.problemId = problemId;
        this.language = language;
        this.status = status;
        this.verdict = verdict;
        this.passedTests = passedTests;
        this.totalTests = totalTests;
        this.executionTime = executionTime;
        this.memoryUsed = memoryUsed;
        this.submittedAt = submittedAt;
    }

    public Integer getSubmissionId() {
        return submissionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getProblemId() {
        return problemId;
    }

    public String getLanguage() {
        return language;
    }

    public String getStatus() {
        return status;
    }

    public String getVerdict() {
        return verdict;
    }

    public Integer getPassedTests() {
        return passedTests;
    }

    public Integer getTotalTests() {
        return totalTests;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public Long getMemoryUsed() {
        return memoryUsed;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
}
