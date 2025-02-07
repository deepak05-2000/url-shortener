package com.urlshortener.redirect.service;

import com.urlshortener.redirect.dtos.ShortenedUrlDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface ShortenedUrlService {

    public ShortenedUrlDTO shortUrl(String originalUrl);

    public String getOriginalUrl(String shortUrl, HttpServletRequest request);
}
