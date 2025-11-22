package com.saas.multitenantplatform.user;

public record UserCreateRequest(String email, String fullName, String passwordHash, String role) {}
