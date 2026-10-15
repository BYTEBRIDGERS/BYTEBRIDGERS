package com.example.BYTEBRIDGERS.controller;

import com.example.BYTEBRIDGERS.dto.ExecutionRequest;
import com.example.BYTEBRIDGERS.dto.ExecutionResponse;
import com.example.BYTEBRIDGERS.service.ExecutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/execution")
public class ExecutionController {

    private final ExecutionService executionService;

    public ExecutionController(ExecutionService executionService) {
        this.executionService = executionService;
    }

    @PostMapping("/run")
    public ResponseEntity<ExecutionResponse> runCode(
            @RequestBody ExecutionRequest request) {

        ExecutionResponse response =
                executionService.executeSubmission(request);

        return ResponseEntity.ok(response);
    }
}
