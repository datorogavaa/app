package com.application.mapper;

import com.application.dto.HomeDTO;
import com.application.model.Home;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface HomeMapper {

    HomeDTO toDto(Home home);

    Home toEntity(HomeDTO homeDto);
}
