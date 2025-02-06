package com.urlshortener.redirect.rest;

import com.urlshortener.redirect.domain.Authority;
import com.urlshortener.redirect.domain.User;
import com.urlshortener.redirect.dtos.AuthRequest;
import com.urlshortener.redirect.dtos.LoginRequest;
import com.urlshortener.redirect.jwt.JwtResponse;
import com.urlshortener.redirect.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest authRequest) throws URISyntaxException {
        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setAuthorities(authRequest.getAuthorities().stream().map(auth -> {
            Authority authority = new Authority();
            authority.setName(auth);
            return authority;
        }).collect(Collectors.toSet()));
        user.setEmail(authRequest.getEmail());
        user.setCreatedBy(authRequest.getCreatedBy());
        return ResponseEntity.created(new URI("/api/register")).body(userService.saveUser(user));

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = userService.loginUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }
}
