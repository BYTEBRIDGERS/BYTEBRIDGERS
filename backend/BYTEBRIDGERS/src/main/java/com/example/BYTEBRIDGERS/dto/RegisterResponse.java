package com.example.BYTEBRIDGERS.dto;

public class RegisterResponse {

    private String message;
    private Integer userId;
    private String username;
    private String email;

    public RegisterResponse(
            String message,
            Integer userId,
            String username,
            String email) {

        this.message = message;
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
