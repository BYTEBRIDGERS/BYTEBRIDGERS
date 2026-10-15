package com.example.BYTEBRIDGERS.dto;

public class LoginResponse {

    private String message;
    private Integer userId;
    private String username;

    public LoginResponse(String message, Integer userId, String username) {
        this.message = message;
        this.userId = userId;
        this.username = username;
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
}
