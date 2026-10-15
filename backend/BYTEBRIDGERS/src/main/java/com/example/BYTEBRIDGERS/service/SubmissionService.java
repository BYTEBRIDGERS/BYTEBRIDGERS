package com.example.BYTEBRIDGERS.service;

import com.example.BYTEBRIDGERS.repository.ProblemRepository;
import com.example.BYTEBRIDGERS.repository.UserRepository;
import com.example.BYTEBRIDGERS.exception.UnauthorizedAccessException;
import com.example.BYTEBRIDGERS.dto.ExecutionRequest;
import com.example.BYTEBRIDGERS.dto.ExecutionResponse;
import com.example.BYTEBRIDGERS.dto.ExecutionTestCase;

import com.example.BYTEBRIDGERS.dto.JudgeRequest;
import com.example.BYTEBRIDGERS.dto.JudgeResponse;

import com.example.BYTEBRIDGERS.dto.SubmissionRequest;
import com.example.BYTEBRIDGERS.dto.SubmissionResponse;

import com.example.BYTEBRIDGERS.entity.Submission;
import com.example.BYTEBRIDGERS.entity.TestCase;

import com.example.BYTEBRIDGERS.repository.SubmissionRepository;
import com.example.BYTEBRIDGERS.repository.TestCaseRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final TestCaseRepository testCaseRepository;
    private final ExecutionClient executionClient;
    private final JudgeClient judgeClient;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;

    public SubmissionService(
            SubmissionRepository submissionRepository,
            TestCaseRepository testCaseRepository,
            ExecutionClient executionClient,
            JudgeClient judgeClient,
            UserRepository userRepository,
            ProblemRepository problemRepository) {

        this.submissionRepository = submissionRepository;
        this.testCaseRepository = testCaseRepository;
        this.executionClient = executionClient;
        this.judgeClient = judgeClient;
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
    }

    public SubmissionResponse createSubmission(
            SubmissionRequest request,
            Integer authenticatedUserId) {

        // The authenticated user must exist
        if (!userRepository.existsById(authenticatedUserId)) {
            throw new RuntimeException("Authenticated user not found");
        }

        // Validate problem
        if (!problemRepository.existsById(request.getProblemId())) {
            throw new RuntimeException("Problem not found");
        }

        // 1. Create submission
        Submission submission = new Submission();

        // IMPORTANT:
        // Ignore request.getUserId().
        // Use the authenticated user's ID instead.
        submission.setUserId(authenticatedUserId);

        submission.setProblemId(request.getProblemId());
        submission.setLanguage(request.getLanguage());
        submission.setSourceCode(request.getSourceCode());

        submission.setStatus("PENDING");
        submission.setVerdict(null);
        submission.setPassedTests(null);
        submission.setTotalTests(null);
        submission.setExecutionTime(null);
        submission.setMemoryUsed(null);
        submission.setSubmittedAt(LocalDateTime.now());

        // 2. Save submission
        Submission savedSubmission =
                submissionRepository.save(submission);

        // Mark submission as executing
        savedSubmission.setStatus("EXECUTING");

        savedSubmission =
                submissionRepository.save(savedSubmission);

        // 3. Fetch hidden test cases
        List<TestCase> testCases =
                testCaseRepository.findByProblemId(
                        savedSubmission.getProblemId());

        System.out.println(
                "Problem ID: " + savedSubmission.getProblemId());

        System.out.println(
                "Test cases found: " + testCases.size());

        // 4. Convert test cases to execution DTOs
        List<ExecutionTestCase> executionTestCases =
                testCases.stream()
                        .map(testCase -> new ExecutionTestCase(
                                testCase.getInput(),
                                testCase.getExpectedOutput(),
                                testCase.getTimeLimit(),
                                testCase.getMemoryLimit()
                        ))
                        .toList();

        // 5. Build P3 execution request
        ExecutionRequest executionRequest =
                new ExecutionRequest(
                        savedSubmission.getSubmissionId(),
                        savedSubmission.getProblemId(),
                        savedSubmission.getLanguage(),
                        savedSubmission.getSourceCode(),
                        executionTestCases
                );

        // 6. Send submission to P3
        ExecutionResponse executionResponse =
                executionClient.sendToP3(executionRequest);

        // 7. Store P3 execution result
        savedSubmission.setStatus(
                executionResponse.getStatus());

        savedSubmission.setExecutionTime(
                executionResponse.getExecutionTime());

        savedSubmission.setMemoryUsed(
                executionResponse.getMemoryUsed());

        // 8. Stop here if P3 execution failed
        if (!"COMPLETED".equalsIgnoreCase(
                executionResponse.getStatus())) {

            Submission failedSubmission =
                    submissionRepository.save(savedSubmission);

            return new SubmissionResponse(
                    failedSubmission.getSubmissionId(),
                    failedSubmission.getUserId(),
                    failedSubmission.getProblemId(),
                    failedSubmission.getLanguage(),
                    failedSubmission.getStatus(),
                    null,
                    null,
                    null,
                    failedSubmission.getExecutionTime(),
                    failedSubmission.getMemoryUsed(),
                    failedSubmission.getSubmittedAt()
            );
        }

        // 9. Get actual outputs from P3
        List<String> actualOutputs =
                executionResponse.getOutputs();

        // 10. Get expected outputs from hidden test cases
        List<String> expectedOutputs =
                testCases.stream()
                        .map(TestCase::getExpectedOutput)
                        .toList();

        // 11. Build P4 judge request
        JudgeRequest judgeRequest =
                new JudgeRequest(
                        savedSubmission.getSubmissionId(),
                        savedSubmission.getProblemId(),
                        savedSubmission.getLanguage(),
                        actualOutputs,
                        expectedOutputs
                );

        System.out.println("========== P2 -> P4 ==========");
        System.out.println(
                "Submission ID: "
                        + judgeRequest.getSubmissionId());

        System.out.println(
                "Problem ID: "
                        + judgeRequest.getProblemId());

        System.out.println(
                "Actual Outputs: "
                        + judgeRequest.getActualOutputs());

        System.out.println(
                "Expected Outputs: "
                        + judgeRequest.getExpectedOutputs());

        System.out.println("==============================");

        // 12. Send outputs to P4
        JudgeResponse judgeResponse =
                judgeClient.sendToP4(judgeRequest);

        System.out.println("========== P4 -> P2 ==========");
        System.out.println(
                "Verdict: "
                        + judgeResponse.getVerdict());

        System.out.println(
                "Passed Tests: "
                        + judgeResponse.getPassedTests());

        System.out.println(
                "Total Tests: "
                        + judgeResponse.getTotalTests());

        System.out.println("==============================");

        // 13. Store P4 judging result
        savedSubmission.setVerdict(
                judgeResponse.getVerdict());

        savedSubmission.setPassedTests(
                judgeResponse.getPassedTests());

        savedSubmission.setTotalTests(
                judgeResponse.getTotalTests());

        // 14. Save final submission
        Submission updatedSubmission =
                submissionRepository.save(savedSubmission);

        // 15. Return final submission
        return new SubmissionResponse(
                updatedSubmission.getSubmissionId(),
                updatedSubmission.getUserId(),
                updatedSubmission.getProblemId(),
                updatedSubmission.getLanguage(),
                updatedSubmission.getStatus(),
                updatedSubmission.getVerdict(),
                updatedSubmission.getPassedTests(),
                updatedSubmission.getTotalTests(),
                updatedSubmission.getExecutionTime(),
                updatedSubmission.getMemoryUsed(),
                updatedSubmission.getSubmittedAt()
        );
    }

    public SubmissionResponse getSubmissionById(
            Integer submissionId,
            Integer authenticatedUserId) {

        Submission submission =
                submissionRepository.findById(submissionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Submission not found"));

        // Authorization check
        if (!submission.getUserId()
                .equals(authenticatedUserId)) {

            throw new UnauthorizedAccessException(
                    "You are not allowed to access this submission");
        }

        return new SubmissionResponse(
                submission.getSubmissionId(),
                submission.getUserId(),
                submission.getProblemId(),
                submission.getLanguage(),
                submission.getStatus(),
                submission.getVerdict(),
                submission.getPassedTests(),
                submission.getTotalTests(),
                submission.getExecutionTime(),
                submission.getMemoryUsed(),
                submission.getSubmittedAt()
        );
    }

    public List<SubmissionResponse> getSubmissionsByUserId(
            Integer userId,
            Integer authenticatedUserId) {

        // Authorization check
        if (!userId.equals(authenticatedUserId)) {
            throw new UnauthorizedAccessException(
                    "You are not allowed to access these submissions");
        }

        List<Submission> submissions =
                submissionRepository.findByUserId(userId);

        return submissions.stream()
                .map(submission -> new SubmissionResponse(
                        submission.getSubmissionId(),
                        submission.getUserId(),
                        submission.getProblemId(),
                        submission.getLanguage(),
                        submission.getStatus(),
                        submission.getVerdict(),
                        submission.getPassedTests(),
                        submission.getTotalTests(),
                        submission.getExecutionTime(),
                        submission.getMemoryUsed(),
                        submission.getSubmittedAt()
                ))
                .toList();
    }
}