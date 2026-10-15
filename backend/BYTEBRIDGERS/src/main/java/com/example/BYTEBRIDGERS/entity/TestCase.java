package com.example.BYTEBRIDGERS.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "testcase")
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer testcaseId;

    @Column(name = "problem_id", nullable = false)
    private Integer problemId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String input;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String expectedOutput;

    @Column(nullable = false)
    private Integer timeLimit;

    @Column(nullable = false)
    private Integer memoryLimit;

    public TestCase() {
    }

    public Integer getTestcaseId() {
        return testcaseId;
    }

    public void setTestcaseId(Integer testcaseId) {
        this.testcaseId = testcaseId;
    }

    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Integer getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(Integer memoryLimit) {
        this.memoryLimit = memoryLimit;
    }
}