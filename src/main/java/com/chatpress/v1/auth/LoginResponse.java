package com.chatpress.v1.auth;

public record LoginResponse(
        String token,
        String username,
        String role
) {}
