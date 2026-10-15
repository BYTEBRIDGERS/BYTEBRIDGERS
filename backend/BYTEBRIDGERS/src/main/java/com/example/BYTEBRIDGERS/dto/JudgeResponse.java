package com.example.BYTEBRIDGERS.dto;

public class JudgeResponse {

    private Integer submissionId;
    private String verdict;
    private Integer passedTests;
    private Integer totalTests;

    public JudgeResponse() {
    }

    public JudgeResponse(
            Integer submissionId,
            String verdict,
            Integer passedTests,
            Integer totalTests) {

        this.submissionId = submissionId;
        this.verdict = verdict;
        this.passedTests = passedTests;
        this.totalTests = totalTests;
    }

    public Integer getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Integer submissionId) {
        this.submissionId = submissionId;
    }

    public String getVerdict() {
        return verdict;
    }

    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }

    public Integer getPassedTests() {
        return passedTests;
    }

    public void setPassedTests(Integer passedTests) {
        this.passedTests = passedTests;
    }

    public Integer getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(Integer totalTests) {
        this.totalTests = totalTests;
    }
}