package com.example.BYTEBRIDGERS.controller;

import com.example.BYTEBRIDGERS.dto.JudgeRequest;
import com.example.BYTEBRIDGERS.dto.JudgeResponse;
import com.example.BYTEBRIDGERS.service.JudgeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/judge")
public class JudgeController {

    private final JudgeService judgeService;

    public JudgeController(JudgeService judgeService) {
        this.judgeService = judgeService;
    }

    @PostMapping
    public ResponseEntity<JudgeResponse> judgeSubmission(
            @RequestBody JudgeRequest request) {

        JudgeResponse response =
                judgeService.judgeSubmission(request);

        return ResponseEntity.ok(response);
    }
}