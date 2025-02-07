package com.urlshortener.redirect.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
public class Analytics implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;

    private String ipAddress;

    private String country;

    private String deviceType;

    private String browser;

    private String referrer;

    @JsonIgnoreProperties("analytics")
    @ManyToOne
    @JoinColumn(name = "short_code")
    private ShortenedUrl shortenedUrl;
}
