package com.example.BYTEBRIDGERS.dto;

import java.util.List;

public class ExecutionResponse {

    private Integer submissionId;
    private String status;
    private String output;
    private List<String> outputs;
    private String error;
    private Long executionTime;
    private Long memoryUsed;

    public ExecutionResponse() {
    }

    public ExecutionResponse(
            Integer submissionId,
            String status,
            String output,
            List<String> outputs,
            String error,
            Long executionTime,
            Long memoryUsed) {

        this.submissionId = submissionId;
        this.status = status;
        this.output = output;
        this.outputs = outputs;
        this.error = error;
        this.executionTime = executionTime;
        this.memoryUsed = memoryUsed;
    }

    public Integer getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Integer submissionId) {
        this.submissionId = submissionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public List<String> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<String> outputs) {
        this.outputs = outputs;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public Long getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(Long memoryUsed) {
        this.memoryUsed = memoryUsed;
    }
}