package com.urlshortener.redirect.service;

import com.urlshortener.redirect.domain.Analytics;
import com.urlshortener.redirect.dtos.AnalyticsDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface AnalyticsService {

    List<AnalyticsDTO> getAnalytics(String shortCode, LocalDateTime startDate, LocalDateTime endDate);

    AnalyticsDTO saveAnalytics(Analytics analytics);
}
