package com.example.BYTEBRIDGERS.dto;

public class DashboardResponse {

    private Integer userId;
    private Long totalSubmissions;
    private Long acceptedSubmissions;
    private Long wrongAnswers;
    private Long problemsSolved;

    public DashboardResponse() {
    }

    public DashboardResponse(
            Integer userId,
            Long totalSubmissions,
            Long acceptedSubmissions,
            Long wrongAnswers,
            Long problemsSolved) {

        this.userId = userId;
        this.totalSubmissions = totalSubmissions;
        this.acceptedSubmissions = acceptedSubmissions;
        this.wrongAnswers = wrongAnswers;
        this.problemsSolved = problemsSolved;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getTotalSubmissions() {
        return totalSubmissions;
    }

    public void setTotalSubmissions(Long totalSubmissions) {
        this.totalSubmissions = totalSubmissions;
    }

    public Long getAcceptedSubmissions() {
        return acceptedSubmissions;
    }

    public void setAcceptedSubmissions(Long acceptedSubmissions) {
        this.acceptedSubmissions = acceptedSubmissions;
    }

    public Long getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(Long wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    public Long getProblemsSolved() {
        return problemsSolved;
    }

    public void setProblemsSolved(Long problemsSolved) {
        this.problemsSolved = problemsSolved;
    }
}