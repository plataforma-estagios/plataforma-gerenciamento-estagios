package com.ufape.estagios.model;

public enum UserRole {
    COMPANY("company"),
    CANDIDATE("candidate");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}