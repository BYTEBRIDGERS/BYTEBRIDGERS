package com.example.BYTEBRIDGERS.dto;

import java.util.List;

public class ExecutionRequest {

    private Integer submissionId;
    private Integer problemId;
    private String language;
    private String sourceCode;
    private List<ExecutionTestCase> testCases;

    public ExecutionRequest() {
    }

    public ExecutionRequest(
            Integer submissionId,
            Integer problemId,
            String language,
            String sourceCode,
            List<ExecutionTestCase> testCases) {

        this.submissionId = submissionId;
        this.problemId = problemId;
        this.language = language;
        this.sourceCode = sourceCode;
        this.testCases = testCases;
    }

    public Integer getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Integer submissionId) {
        this.submissionId = submissionId;
    }

    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public List<ExecutionTestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<ExecutionTestCase> testCases) {
        this.testCases = testCases;
    }
}
