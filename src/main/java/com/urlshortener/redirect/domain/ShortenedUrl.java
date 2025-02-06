package com.urlshortener.redirect.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class ShortenedUrl {
    @Id
    private String shortCode;

    @Column(nullable = false, unique = true)
    private String originalUrl;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime expiresAt;

    private Long clickCount;


    //TODO make it lazy intialize and make DTOs so that serialization works
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @JsonIgnore
    @OneToMany(mappedBy = "shortenedUrl", cascade = CascadeType.ALL)
    private List<Analytics> analytics = new ArrayList<>();

}
