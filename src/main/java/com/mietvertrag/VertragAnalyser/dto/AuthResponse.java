package com.mietvertrag.VertragAnalyser.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String email;
    private String name;
    private Long userId;

    public AuthResponse(String token, String email, String name, Long id) {
        this.token = token;
        this.email = email;
        this.name = name;
        this.userId = id;
    }
}
