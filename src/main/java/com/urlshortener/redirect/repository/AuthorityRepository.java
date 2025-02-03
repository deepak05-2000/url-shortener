package com.urlshortener.redirect.repository;

import com.urlshortener.redirect.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {

}
