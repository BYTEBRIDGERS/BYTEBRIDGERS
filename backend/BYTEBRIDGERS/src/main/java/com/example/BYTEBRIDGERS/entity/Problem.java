package com.example.BYTEBRIDGERS.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Problem")
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer problemId;

    @Column(nullable = false)
    private String problemName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String difficulty;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String inputDescription;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String outputDescription;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String constraints;

    public Problem() {
    }

    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getInputDescription() {
        return inputDescription;
    }

    public void setInputDescription(String inputDescription) {
        this.inputDescription = inputDescription;
    }

    public String getOutputDescription() {
        return outputDescription;
    }

    public void setOutputDescription(String outputDescription) {
        this.outputDescription = outputDescription;
    }

    public String getConstraints() {
        return constraints;
    }

    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }
}
