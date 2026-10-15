package com.example.BYTEBRIDGERS.dto;

import java.util.List;

public class JudgeRequest {

    private Integer submissionId;
    private Integer problemId;
    private String language;
    private List<String> actualOutputs;
    private List<String> expectedOutputs;

    public JudgeRequest() {
    }

    public JudgeRequest(
            Integer submissionId,
            Integer problemId,
            String language,
            List<String> actualOutputs,
            List<String> expectedOutputs) {

        this.submissionId = submissionId;
        this.problemId = problemId;
        this.language = language;
        this.actualOutputs = actualOutputs;
        this.expectedOutputs = expectedOutputs;
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

    public List<String> getActualOutputs() {
        return actualOutputs;
    }

    public void setActualOutputs(List<String> actualOutputs) {
        this.actualOutputs = actualOutputs;
    }

    public List<String> getExpectedOutputs() {
        return expectedOutputs;
    }

    public void setExpectedOutputs(List<String> expectedOutputs) {
        this.expectedOutputs = expectedOutputs;
    }
}