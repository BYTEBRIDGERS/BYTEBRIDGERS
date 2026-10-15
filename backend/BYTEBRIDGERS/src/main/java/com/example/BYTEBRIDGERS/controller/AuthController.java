package com.example.BYTEBRIDGERS.controller;

import com.example.BYTEBRIDGERS.dto.LoginRequest;
import com.example.BYTEBRIDGERS.dto.LoginResponse;
import com.example.BYTEBRIDGERS.dto.RegisterRequest;
import com.example.BYTEBRIDGERS.dto.RegisterResponse;
import com.example.BYTEBRIDGERS.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final SecurityContextRepository securityContextRepository;

    public AuthController(
            AuthService authService,
            SecurityContextRepository securityContextRepository) {

        this.authService = authService;
        this.securityContextRepository = securityContextRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        LoginResponse response = authService.login(request);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        response.getUserId().toString(),
                        null,
                        null
                );

        SecurityContext securityContext =
                SecurityContextHolder.createEmptyContext();

        securityContext.setAuthentication(authentication);

        SecurityContextHolder.setContext(securityContext);

        securityContextRepository.saveContext(
                securityContext,
                httpRequest,
                httpResponse
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        RegisterResponse response =
                authService.register(request);

        return ResponseEntity.ok(response);
    }
}