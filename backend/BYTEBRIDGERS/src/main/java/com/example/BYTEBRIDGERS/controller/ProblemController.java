package com.example.BYTEBRIDGERS.controller;

import com.example.BYTEBRIDGERS.dto.ProblemResponse;
import com.example.BYTEBRIDGERS.service.ProblemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping
    public ResponseEntity<List<ProblemResponse>> getAllProblems() {
        return ResponseEntity.ok(problemService.getAllProblems());
    }

    @GetMapping("/{problemId}")
    public ResponseEntity<ProblemResponse> getProblemById(
            @PathVariable Integer problemId) {

        return ResponseEntity.ok(
                problemService.getProblemById(problemId)
        );
}
}
