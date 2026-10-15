package com.example.BYTEBRIDGERS.service;

import com.example.BYTEBRIDGERS.dto.ProblemResponse;
import com.example.BYTEBRIDGERS.entity.Problem;
import com.example.BYTEBRIDGERS.repository.ProblemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemService {

    private final ProblemRepository problemRepository;

    public ProblemService(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    public List<ProblemResponse> getAllProblems() {

        List<Problem> problems = problemRepository.findAll();

        return problems.stream()
                .map(problem -> new ProblemResponse(
                        problem.getProblemId(),
                        problem.getProblemName(),
                        problem.getDescription(),
                        problem.getDifficulty(),
                        problem.getInputDescription(),
                        problem.getOutputDescription(),
                        problem.getConstraints()
                ))
                .toList();
    }
    public ProblemResponse getProblemById(Integer problemId) {

    Problem problem = problemRepository.findById(problemId)
            .orElseThrow(() ->
                    new RuntimeException("Problem not found"));

    return new ProblemResponse(
            problem.getProblemId(),
            problem.getProblemName(),
            problem.getDescription(),
            problem.getDifficulty(),
            problem.getInputDescription(),
            problem.getOutputDescription(),
            problem.getConstraints()
    );
}
}
