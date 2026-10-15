package com.example.BYTEBRIDGERS.dto;

public class ProblemResponse {

    private Integer problemId;
    private String problemName;
    private String description;
    private String difficulty;
    private String inputDescription;
    private String outputDescription;
    private String constraints;

    public ProblemResponse(
            Integer problemId,
            String problemName,
            String description,
            String difficulty,
            String inputDescription,
            String outputDescription,
            String constraints) {

        this.problemId = problemId;
        this.problemName = problemName;
        this.description = description;
        this.difficulty = difficulty;
        this.inputDescription = inputDescription;
        this.outputDescription = outputDescription;
        this.constraints = constraints;
    }

    public Integer getProblemId() {
        return problemId;
    }

    public String getProblemName() {
        return problemName;
    }

    public String getDescription() {
        return description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getInputDescription() {
        return inputDescription;
    }

    public String getOutputDescription() {
        return outputDescription;
    }

    public String getConstraints() {
        return constraints;
    }
}
