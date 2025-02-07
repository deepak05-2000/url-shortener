package com.urlshortener.redirect.service.mapper;

import com.urlshortener.redirect.domain.ShortenedUrl;
import com.urlshortener.redirect.dtos.ShortenedUrlDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ShortenedUrlMapper {

    ShortenedUrl toEntity(ShortenedUrlDTO shortenedUrlDTO);
    ShortenedUrlDTO toDTO(ShortenedUrl shortenedUrl);
}
