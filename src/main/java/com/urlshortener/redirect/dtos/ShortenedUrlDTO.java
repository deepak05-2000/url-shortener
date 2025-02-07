package com.urlshortener.redirect.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShortenedUrlDTO {

    private String shortCode;

    private String originalUrl;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private Long clickCount;

    private UserDTO owner;

}
