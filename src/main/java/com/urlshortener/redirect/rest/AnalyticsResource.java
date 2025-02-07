package com.urlshortener.redirect.rest;

import com.urlshortener.redirect.dtos.AnalyticsDTO;
import com.urlshortener.redirect.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AnalyticsResource {

    private AnalyticsService analyticsService;

    public AnalyticsResource(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/analytics/{shortCode}")
    public ResponseEntity<List<AnalyticsDTO>> getAnalytics(
            @PathVariable String shortCode,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate
    ) {
        return ResponseEntity.ok(analyticsService.getAnalytics(shortCode, startDate, endDate));

    }
}
