package com.urlshortener.redirect.rest;

import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminResource {

    @GetMapping("/hello")
    public ResponseEntity<String> admin(HttpServletRequest request) {

        String userAgentString = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
        log.debug("USER AGENT {}", userAgentString);
        log.debug("USER AGENT {}", userAgent);
        return ResponseEntity.ok(userAgentString);
    }
}
