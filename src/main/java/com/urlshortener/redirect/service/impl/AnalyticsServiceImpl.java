package com.urlshortener.redirect.service.impl;

import com.urlshortener.redirect.domain.Analytics;
import com.urlshortener.redirect.dtos.AnalyticsDTO;
import com.urlshortener.redirect.repository.AnalyticsRepository;
import com.urlshortener.redirect.service.AnalyticsService;
import com.urlshortener.redirect.service.mapper.AnalyticsMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnalyticsServiceImpl implements AnalyticsService {

    private AnalyticsRepository analyticsRepository;
    private AnalyticsMapper analyticsMapper;

    public AnalyticsServiceImpl(AnalyticsRepository analyticsRepository, AnalyticsMapper analyticsMapper) {
        this.analyticsRepository = analyticsRepository;
        this.analyticsMapper = analyticsMapper;
    }

    @Override
    @Cacheable(value = "analytics", key = "#shortCode + '.analytics'")
    public List<AnalyticsDTO> getAnalytics(String shortCode, LocalDateTime startDate, LocalDateTime endDate) {
        List<Analytics> analytics;
        if(startDate == null && endDate == null) {
            analytics = analyticsRepository.findByShortenedUrlShortCode(shortCode);
        }
        else {
            analytics = analyticsRepository.findByShortenedUrlShortCodeAndTimestampBetween(shortCode, startDate, endDate);
        }
        return analytics.stream().map(analyticsMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public AnalyticsDTO saveAnalytics(Analytics analytics) {
        analytics = analyticsRepository.save(analytics);
        return analyticsMapper.toDTO(analytics);
    }
}
