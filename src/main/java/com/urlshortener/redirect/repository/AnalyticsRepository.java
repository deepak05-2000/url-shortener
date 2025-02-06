package com.urlshortener.redirect.repository;

import com.urlshortener.redirect.domain.Analytics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {
    List<Analytics> findByShortenedUrlShortCodeAndTimestampBetween(
            String shortenedUrl,
            LocalDateTime start,
            LocalDateTime end
    );
    List<Analytics> findByShortenedUrlShortCode(String shortUrl);
}
