package com.urlshortener.redirect.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class AuthRequest {
    private String username;
    private String password;
    private String email;
    private String createdBy;
    private Set<String> authorities;

}
