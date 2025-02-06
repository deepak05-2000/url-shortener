package com.urlshortener.redirect.rest;

import com.urlshortener.redirect.service.AnalyticsService;
import com.urlshortener.redirect.service.ShortenedUrlService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class ShortenedUrlResource {

    private final ShortenedUrlService urlService;

    private final AnalyticsService analyticsService;

    public ShortenedUrlResource(ShortenedUrlService urlService, AnalyticsService analyticsService) {
        this.urlService = urlService;
        this.analyticsService = analyticsService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody String longUrl, HttpServletRequest request) {
//        if (!rateLimitingConfig.tryConsume(request.getRemoteAddr())) {
//            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
//        }
        return ResponseEntity.ok(urlService.shortUrl(longUrl));
    }

    @GetMapping("/redirect/{shortCode}")
    public ResponseEntity<?> redirect(@PathVariable String shortCode, HttpServletRequest request) {
        try {
            String longUrl = urlService.getOriginalUrl(shortCode, request);
            if (!longUrl.startsWith("http")) {
                longUrl = "https://" + longUrl;
            }
            log.debug("Redirecting to: {}", longUrl);
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                                 .header("Location", longUrl)
                                 .build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
