package com.urlshortener.redirect.service;

import com.urlshortener.redirect.domain.Authority;
import com.urlshortener.redirect.domain.User;
import com.urlshortener.redirect.dtos.LoginRequest;
import com.urlshortener.redirect.jwt.JwtResponse;
import com.urlshortener.redirect.jwt.JwtUtil;
import com.urlshortener.redirect.repository.AuthorityRepository;
import com.urlshortener.redirect.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class UserService {

    private UserRepository userRepository;
    private JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;
    private AuthorityRepository authorityRepository;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil, AuthenticationManager authenticationManager,
            AuthorityRepository authorityRepository
    ) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.authorityRepository = authorityRepository;
    }

    public User saveUser(User user) {
        List<String> dbAuthorities = authorityRepository.findAll()
                .stream().map(Authority::getName).toList();
        List<String> userAuthorities = user.getAuthorities()
                                           .stream().map(GrantedAuthority::getAuthority).toList();
        for(String authority : userAuthorities) {
            if(!dbAuthorities.contains(authority)) {
                Authority authorityToSave = new Authority();
                authorityToSave.setName(authority);
                authorityRepository.save(authorityToSave);
            }
        }
        return userRepository.save(user);
    }


    public JwtResponse loginUser(LoginRequest loginRequest) {

        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(user);
        return new JwtResponse(token);
    }
}
