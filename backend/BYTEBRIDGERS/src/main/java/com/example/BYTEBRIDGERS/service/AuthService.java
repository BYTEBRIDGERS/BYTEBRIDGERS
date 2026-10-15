package com.example.BYTEBRIDGERS.service;

import com.example.BYTEBRIDGERS.dto.LoginRequest;
import com.example.BYTEBRIDGERS.dto.LoginResponse;
import com.example.BYTEBRIDGERS.dto.RegisterRequest;
import com.example.BYTEBRIDGERS.dto.RegisterResponse;
import com.example.BYTEBRIDGERS.entity.User;
import com.example.BYTEBRIDGERS.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(
                request.getEmail());

        if (user == null) {
            throw new RuntimeException(
            "Invalid email or password");
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException("Invalid email or password");
        }

        return new LoginResponse(
                "Login successful",
                user.getUserId(),
                user.getUsername()
        );
    }

    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(
        request.getUsername())) {

        throw new RuntimeException(
            "Username already exists");
        }

        if (userRepository.existsByEmail(
        request.getEmail())) {

       throw new RuntimeException(
            "Email already exists");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        String hashedPassword =
                passwordEncoder.encode(request.getPassword());

        user.setPassword(hashedPassword);

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                "Registration successful",
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );
    }
}
