package com.application.mapper;

import com.application.dto.HomeDTO;
import com.application.model.Home;
import org.mapstruct.Mapping;

public interface HomeMapper {

    @Mapping(source = "user.id", target = "userId")
    HomeDTO toDto(Home home);

    @Mapping(source = "UserId" , target = "user.id")
    Home toEntity(HomeDTO homeDto);
}
