package com.example.BYTEBRIDGERS.service;

import com.example.BYTEBRIDGERS.dto.ExecutionRequest;
import com.example.BYTEBRIDGERS.dto.ExecutionResponse;
import com.example.BYTEBRIDGERS.dto.ExecutionTestCase;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ExecutionService {

    public ExecutionResponse executeSubmission(
            ExecutionRequest request) {

        long startTime = System.currentTimeMillis();

        Path workspace = null;

        try {

            // 1. Check language
            if (!"CPP".equalsIgnoreCase(request.getLanguage())) {

                return new ExecutionResponse(
                        request.getSubmissionId(),
                        "FAILED",
                        null,
                        new ArrayList<>(),
                        "Unsupported language",
                        0L,
                        0L
                );
            }

            // 2. Create temporary workspace
            workspace =
                    Files.createTempDirectory(
                            "bytebridgers-submission-"
                    );

            Path sourceFile =
                    workspace.resolve("source.cpp");

            Path executable =
                    workspace.resolve("program.exe");

            // 3. Write submitted source code
            Files.writeString(
                    sourceFile,
                    request.getSourceCode(),
                    StandardCharsets.UTF_8
            );

            // 4. Compile
            Process compileProcess =
                    new ProcessBuilder(
                            "g++",
                            sourceFile.toString(),
                            "-o",
                            executable.toString()
                    )
                            .redirectErrorStream(true)
                            .start();

            String compileOutput =
                    new String(
                            compileProcess
                                    .getInputStream()
                                    .readAllBytes(),
                            StandardCharsets.UTF_8
                    );

            boolean compileFinished =
                    compileProcess.waitFor(
                            10,
                            TimeUnit.SECONDS
                    );

            if (!compileFinished) {

                compileProcess.destroyForcibly();

                return new ExecutionResponse(
                        request.getSubmissionId(),
                        "COMPILATION_ERROR",
                        null,
                        new ArrayList<>(),
                        "Compilation timed out",
                        elapsed(startTime),
                        0L
                );
            }

            if (compileProcess.exitValue() != 0) {

                return new ExecutionResponse(
                        request.getSubmissionId(),
                        "COMPILATION_ERROR",
                        null,
                        new ArrayList<>(),
                        compileOutput,
                        elapsed(startTime),
                        0L
                );
            }

            // 5. Execute each test case
            List<String> outputs =
                    new ArrayList<>();

            for (ExecutionTestCase testCase :
                    request.getTestCases()) {

                Process runProcess =
                        new ProcessBuilder(
                                executable.toString()
                        )
                                .redirectErrorStream(true)
                                .start();

                // Send input to program
                runProcess
                        .getOutputStream()
                        .write(
                                testCase.getInput()
                                        .getBytes(StandardCharsets.UTF_8)
                        );

                runProcess.getOutputStream().close();

                // Use test-case time limit
                int timeLimit =
                        testCase.getTimeLimit() != null
                                ? testCase.getTimeLimit()
                                : 2000;

                boolean finished =
                        runProcess.waitFor(
                                timeLimit,
                                TimeUnit.MILLISECONDS
                        );

                if (!finished) {

                    runProcess.destroyForcibly();

                    return new ExecutionResponse(
                            request.getSubmissionId(),
                            "TIME_LIMIT_EXCEEDED",
                            null,
                            outputs,
                            "Program exceeded time limit",
                            elapsed(startTime),
                            0L
                    );
                }

                String output =
                        new String(
                                runProcess
                                        .getInputStream()
                                        .readAllBytes(),
                                StandardCharsets.UTF_8
                        );

                if (runProcess.exitValue() != 0) {

                    return new ExecutionResponse(
                            request.getSubmissionId(),
                            "RUNTIME_ERROR",
                            null,
                            outputs,
                            output,
                            elapsed(startTime),
                            0L
                    );
                }

                outputs.add(output);
            }

            // 6. Successful execution
            String firstOutput =
                    outputs.isEmpty()
                            ? null
                            : outputs.get(0);

            return new ExecutionResponse(
                    request.getSubmissionId(),
                    "COMPLETED",
                    firstOutput,
                    outputs,
                    null,
                    elapsed(startTime),
                    0L
            );

        } catch (Exception exception) {

            return new ExecutionResponse(
                    request.getSubmissionId(),
                    "FAILED",
                    null,
                    new ArrayList<>(),
                    exception.getMessage(),
                    elapsed(startTime),
                    0L
            );

        } finally {

            deleteWorkspace(workspace);
        }
    }

    private long elapsed(long startTime) {

        return System.currentTimeMillis() - startTime;
    }

    private void deleteWorkspace(Path workspace) {

        if (workspace == null) {
            return;
        }

        try {

            Files.walk(workspace)
                    .sorted(
                            java.util.Comparator
                                    .reverseOrder()
                    )
                    .forEach(path -> {

                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ignored) {
                        }

                    });

        } catch (IOException ignored) {
        }
    }
}
