
package com.example.bytebridgersp4api.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/internal")
public class JudgeController {

    @PostMapping("/judge")
    public JudgeResponse judge(@RequestBody JudgeRequest request) {
        int passed = 0;
        int total = Math.min(
                request.actualOutputs.size(),
                request.expectedOutputs.size()
        );

        String verdict = "ACCEPTED";

        for (int i = 0; i < total; i++) {
            if (request.actualOutputs.get(i) != null
                    && request.expectedOutputs.get(i) != null
                    && request.actualOutputs.get(i).trim()
                    .equals(request.expectedOutputs.get(i).trim())) {
                passed++;
            } else {
                verdict = "WRONG_ANSWER";
            }
        }

        if (passed != total) {
            verdict = "WRONG_ANSWER";
        }

        return new JudgeResponse(verdict, passed, total);
    }

    public static class JudgeRequest {
        public int submissionId;
        public int problemId;
        public String language;
        public List<String> actualOutputs;
        public List<String> expectedOutputs;
    }

    public static class JudgeResponse {
        public String verdict;
        public int passedTests;
        public int totalTests;

        public JudgeResponse(String verdict, int passedTests, int totalTests) {
            this.verdict = verdict;
            this.passedTests = passedTests;
            this.totalTests = totalTests;
        }
    }
}



