package com.urlshortener.redirect.repository;

import com.urlshortener.redirect.domain.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, String> {

    boolean existsByShortCode(String shortCode);

    Optional<ShortenedUrl> findByShortCode(String shortCode);

    Optional<ShortenedUrl> findByOriginalUrl(String shortCode);
}
