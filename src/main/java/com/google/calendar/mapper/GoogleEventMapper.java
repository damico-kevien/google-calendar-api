package com.google.calendar.mapper;

import com.google.calendar.model.dto.GoogleEventDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface GoogleEventMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromDto(GoogleEventDto dto, @MappingTarget GoogleEventDto event);
}
