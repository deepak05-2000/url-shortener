package com.urlshortener.redirect.service.impl;

import com.urlshortener.redirect.domain.Analytics;
import com.urlshortener.redirect.domain.ShortenedUrl;
import com.urlshortener.redirect.domain.User;
import com.urlshortener.redirect.repository.ShortenedUrlRepository;
import com.urlshortener.redirect.repository.UserRepository;
import com.urlshortener.redirect.service.AnalyticsService;
import com.urlshortener.redirect.service.ShortenedUrlService;
import com.urlshortener.redirect.util.Base62Converter;
import com.urlshortener.redirect.util.SecurityUtil;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class ShortenedUrlServiceImpl implements ShortenedUrlService {

    private final AnalyticsService analyticsService;
    private final ShortenedUrlRepository shortenedUrlRepository;
    private final UserRepository userRepository;

    public ShortenedUrlServiceImpl(AnalyticsService analyticsService, ShortenedUrlRepository shortenedUrlRepository,
            UserRepository userRepository
    ) {
        this.analyticsService = analyticsService;
        this.shortenedUrlRepository = shortenedUrlRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ShortenedUrl shortUrl(String originalUrl) {
        Optional<ShortenedUrl> exist = shortenedUrlRepository.findByOriginalUrl(originalUrl);
        if(exist.isPresent()) {
            return exist.get();
        }
        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setClickCount(0L);
        shortenedUrl.setOriginalUrl(originalUrl);
        shortenedUrl.setShortCode(Base62Converter.generateShortUrl(originalUrl));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(SecurityUtil.getCurrentUsername()).get();
        shortenedUrl.setOwner(user);

        return shortenedUrlRepository.save(shortenedUrl);
    }

    @Override
    public String getOriginalUrl(String shortUrl, HttpServletRequest request) {
        ShortenedUrl shortenedUrl = shortenedUrlRepository.findByShortCode(shortUrl).orElseThrow(
                () -> new RuntimeException("URL does not exist")
        );
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
}
