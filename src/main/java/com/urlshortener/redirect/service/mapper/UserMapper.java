package com.urlshortener.redirect.service.mapper;

import com.urlshortener.redirect.domain.User;
import com.urlshortener.redirect.dtos.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
}
