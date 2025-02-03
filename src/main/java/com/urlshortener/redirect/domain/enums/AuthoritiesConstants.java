package com.urlshortener.redirect.domain.enums;

public enum AuthoritiesConstants {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String role;
    AuthoritiesConstants(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
