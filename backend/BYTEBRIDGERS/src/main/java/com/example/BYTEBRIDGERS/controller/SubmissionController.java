package com.example.BYTEBRIDGERS.controller;

import com.example.BYTEBRIDGERS.dto.SubmissionRequest;
import com.example.BYTEBRIDGERS.dto.SubmissionResponse;
import com.example.BYTEBRIDGERS.exception.UnauthorizedAccessException;
import com.example.BYTEBRIDGERS.service.SubmissionService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(
            SubmissionService submissionService) {

        this.submissionService = submissionService;
    }

    @PostMapping
    public ResponseEntity<SubmissionResponse> createSubmission(
            @Valid @RequestBody SubmissionRequest request,
            Authentication authentication) {

        Integer authenticatedUserId =
                Integer.parseInt(authentication.getName());

        SubmissionResponse response =
                submissionService.createSubmission(
                        request,
                        authenticatedUserId
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<SubmissionResponse> getSubmissionById(
            @PathVariable Integer submissionId,
            Authentication authentication) {

        Integer authenticatedUserId =
                Integer.parseInt(authentication.getName());

        return ResponseEntity.ok(
                submissionService.getSubmissionById(
                        submissionId,
                        authenticatedUserId
                )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubmissionResponse>> getSubmissionsByUserId(
            @PathVariable Integer userId,
            Authentication authentication) {

        Integer authenticatedUserId =
                Integer.parseInt(authentication.getName());

        return ResponseEntity.ok(
                submissionService.getSubmissionsByUserId(
                        userId,
                        authenticatedUserId
                )
        );
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedAccess(
            UnauthorizedAccessException exception) {

        Map<String, String> error = new HashMap<>();

        error.put("error", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(
            RuntimeException exception) {

        Map<String, String> error = new HashMap<>();

        error.put("error", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}