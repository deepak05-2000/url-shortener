package com.urlshortener.redirect.dtos;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AnalyticsDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;

    private LocalDateTime timestamp;

    private String ipAddress;

    private String country;

    private String deviceType;

    private String browser;

    private String referrer;

    private ShortenedUrlDTO shortenedUrlDTO;
}
