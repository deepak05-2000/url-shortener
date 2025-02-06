package com.urlshortener.redirect.service.impl;

import com.urlshortener.redirect.domain.Analytics;
import com.urlshortener.redirect.repository.AnalyticsRepository;
import com.urlshortener.redirect.service.AnalyticsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AnalyticsServiceImpl implements AnalyticsService {

    private AnalyticsRepository analyticsRepository;

    public AnalyticsServiceImpl(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    @Override
    public List<Analytics> getAnalytics(String shortCode, LocalDateTime startDate, LocalDateTime endDate) {
        if(startDate == null && endDate == null) {
            return analyticsRepository.findByShortenedUrlShortCode(shortCode);
        }
        return analyticsRepository.findByShortenedUrlShortCodeAndTimestampBetween(shortCode, startDate, endDate);
    }

    @Override
    public Analytics saveAnalytics(Analytics analytics) {
        return analyticsRepository.save(analytics);
    }
}
