package com.example.BYTEBRIDGERS.service;

import com.example.BYTEBRIDGERS.entity.TestCase;
import com.example.BYTEBRIDGERS.repository.TestCaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestCaseService {

    private final TestCaseRepository testCaseRepository;

    public TestCaseService(TestCaseRepository testCaseRepository) {
        this.testCaseRepository = testCaseRepository;
    }

    public List<TestCase> getTestCasesByProblemId(Integer problemId) {

        return testCaseRepository.findByProblemId(problemId);
    }
}
