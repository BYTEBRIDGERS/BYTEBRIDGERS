package com.example.BYTEBRIDGERS.service;

import com.example.BYTEBRIDGERS.dto.JudgeRequest;
import com.example.BYTEBRIDGERS.dto.JudgeResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class JudgeClient {

    private final RestClient restClient;

    public JudgeClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    public JudgeResponse sendToP4(
            JudgeRequest request) {

        return restClient.post()
                .uri("/internal/judge")
                .body(request)
                .retrieve()
                .body(JudgeResponse.class);
    }
}
