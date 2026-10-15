package com.example.BYTEBRIDGERS.dto;

public class ExecutionTestCase {

    private String input;
    private String expectedOutput;
    private Integer timeLimit;
    private Integer memoryLimit;

    public ExecutionTestCase() {
    }

    public ExecutionTestCase(
            String input,
            String expectedOutput,
            Integer timeLimit,
            Integer memoryLimit) {

        this.input = input;
        this.expectedOutput = expectedOutput;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
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