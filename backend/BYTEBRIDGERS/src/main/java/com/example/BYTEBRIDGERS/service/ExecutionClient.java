package com.example.BYTEBRIDGERS.service;

import com.example.BYTEBRIDGERS.dto.ExecutionRequest;
import com.example.BYTEBRIDGERS.dto.ExecutionResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ExecutionClient {

    private final RestClient restClient;

    public ExecutionClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    public ExecutionResponse sendToP3(
            ExecutionRequest request) {

        return restClient.post()
                .uri("/internal/execution/run")
                .body(request)
                .retrieve()
                .body(ExecutionResponse.class);
    }
}