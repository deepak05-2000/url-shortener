package com.urlshortener.redirect.service.impl;

import com.urlshortener.redirect.domain.Analytics;
import com.urlshortener.redirect.domain.ShortenedUrl;
import com.urlshortener.redirect.domain.User;
import com.urlshortener.redirect.dtos.ShortenedUrlDTO;
import com.urlshortener.redirect.repository.ShortenedUrlRepository;
import com.urlshortener.redirect.repository.UserRepository;
import com.urlshortener.redirect.service.AnalyticsService;
import com.urlshortener.redirect.service.ShortenedUrlService;
import com.urlshortener.redirect.service.mapper.ShortenedUrlMapper;
import com.urlshortener.redirect.util.Base62Converter;
import com.urlshortener.redirect.util.SecurityUtil;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class ShortenedUrlServiceImpl implements ShortenedUrlService {

    private final AnalyticsService analyticsService;
    private final ShortenedUrlRepository shortenedUrlRepository;
    private final UserRepository userRepository;
    private final ShortenedUrlMapper shortenedUrlMapper;

    @Value("${url.expirationDays}")
    private String expirationDays;

    public ShortenedUrlServiceImpl(AnalyticsService analyticsService, ShortenedUrlRepository shortenedUrlRepository,
            UserRepository userRepository, ShortenedUrlMapper shortenedUrlMapper
    ) {
        this.analyticsService = analyticsService;
        this.shortenedUrlRepository = shortenedUrlRepository;
        this.userRepository = userRepository;
        this.shortenedUrlMapper = shortenedUrlMapper;
    }

    @Override
    public ShortenedUrlDTO shortUrl(String originalUrl) {
        Optional<ShortenedUrl> exist = shortenedUrlRepository.findByOriginalUrl(originalUrl);
        if(exist.isPresent()) {
            return shortenedUrlMapper.toDTO(exist.get());
        }
        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setClickCount(0L);
        shortenedUrl.setOriginalUrl(originalUrl);
        shortenedUrl.setExpiresAt(expirationDays != null ? LocalDateTime.now().plusDays(Integer.parseInt(expirationDays)) : null);
        shortenedUrl.setShortCode(Base62Converter.generateShortUrl(originalUrl));
        User user = userRepository.findByUsername(SecurityUtil.getCurrentUsername()).get();
        shortenedUrl.setOwner(user);

        return shortenedUrlMapper.toDTO(shortenedUrlRepository.save(shortenedUrl));
    }

    @Override
    @Cacheable(value = "originalUrl", key = "#shortUrl")
    public String getOriginalUrl(String shortUrl, HttpServletRequest request) {
        log.debug("Entered in getOriginalUrl");
        ShortenedUrl shortenedUrl = shortenedUrlRepository.findByShortCode(shortUrl).orElseThrow(
                () -> new RuntimeException("URL does not exist")
        );
        if (shortenedUrl.getExpiresAt() != null && shortenedUrl.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.GONE, "Short URL has expired");
        }
        log.debug("ORIGINAL URL {}", shortenedUrl.getOriginalUrl());
        shortenedUrl.setClickCount(shortenedUrl.getClickCount() + 1);
        shortenedUrl = shortenedUrlRepository.save(shortenedUrl);
        saveAnalytics(shortenedUrl, request);
        return shortenedUrl.getOriginalUrl();
    }

    private void saveAnalytics(ShortenedUrl shortenedUrl, HttpServletRequest request) {
        Analytics analytics = new Analytics();
        analytics.setIpAddress(getClientIp(request));
        analytics.setShortenedUrl(shortenedUrl);
        analytics.setTimestamp(LocalDateTime.now());

        String userAgentString = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);

        String browser = userAgent.getBrowser().getName();
        DeviceType device = userAgent.getOperatingSystem().getDeviceType();
        analytics.setDeviceType(device.name());
        analytics.setBrowser(browser);
        analyticsService.saveAnalytics(analytics);
    }

    public String getClientIp(HttpServletRequest request) {
        String[] headersToCheck = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "HTTP_VIA",
                "REMOTE_ADDR"
        };

        for (String header : headersToCheck) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }
        return request.getRemoteAddr();
    }

    @Scheduled(cron = "0 0 0 * * ?")  // Runs at 00:00 UTC daily
    public void deleteExpiredShortUrls() {
        shortenedUrlRepository.deleteByExpiresAtBefore(LocalDateTime.now());
        System.out.println("Expired short URLs deleted at midnight.");
    }
}
