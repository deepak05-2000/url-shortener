package com.urlshortener.redirect.service;

import com.urlshortener.redirect.domain.ShortenedUrl;
import jakarta.servlet.http.HttpServletRequest;

public interface ShortenedUrlService {

    public ShortenedUrl shortUrl(String originalUrl);

    public String getOriginalUrl(String shortUrl, HttpServletRequest request);

}
