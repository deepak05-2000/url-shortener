package com.urlshortener.redirect.service;

import com.urlshortener.redirect.domain.Analytics;

import java.time.LocalDateTime;
import java.util.List;

public interface AnalyticsService {
    public List<Analytics> getAnalytics(String shortCode, LocalDateTime startDate, LocalDateTime endDate);

    public Analytics saveAnalytics(Analytics analytics);
}
