package com.example.BYTEBRIDGERS.service;

import com.example.BYTEBRIDGERS.dto.JudgeRequest;
import com.example.BYTEBRIDGERS.dto.JudgeResponse;
import org.springframework.stereotype.Service;

@Service
public class JudgeService {

    public JudgeResponse judgeSubmission(
            JudgeRequest request) {

        int totalTests =
                request.getExpectedOutputs().size();

        int passedTests = 0;

        for (int i = 0; i < totalTests; i++) {

            String actual =
                    normalizeOutput(
                            request.getActualOutputs().get(i));

            String expected =
                    normalizeOutput(
                            request.getExpectedOutputs().get(i));

            if (actual.equals(expected)) {
                passedTests++;
            }
        }

        String verdict;

        if (totalTests == 0) {
            verdict = "NO_TEST_CASES";
        } else if (passedTests == totalTests) {
            verdict = "ACCEPTED";
        } else {
            verdict = "WRONG_ANSWER";
        }

        return new JudgeResponse(
                request.getSubmissionId(),
                verdict,
                passedTests,
                totalTests
        );
    }

    private String normalizeOutput(String output) {

        if (output == null) {
            return "";
        }

        return output
                .replaceAll("\\s+", " ")
                .trim();
    }
}