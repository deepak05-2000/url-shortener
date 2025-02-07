package com.urlshortener.redirect.service.mapper;

import com.urlshortener.redirect.domain.Analytics;
import com.urlshortener.redirect.dtos.AnalyticsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnalyticsMapper {

    Analytics toEntity(AnalyticsDTO analyticsDTO);
    AnalyticsDTO toDTO(Analytics analytics);
}
