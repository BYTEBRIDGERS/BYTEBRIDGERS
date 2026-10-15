package com.example.BYTEBRIDGERS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SubmissionRequest {

    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotNull(message = "Problem ID is required")
    private Integer problemId;

    @NotBlank(message = "Language is required")
    private String language;

    @NotBlank(message = "Source code is required")
    private String sourceCode;

    public SubmissionRequest() {
    }

    public SubmissionRequest(
            Integer userId,
            Integer problemId,
            String language,
            String sourceCode) {

        this.userId = userId;
        this.problemId = problemId;
        this.language = language;
        this.sourceCode = sourceCode;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
}