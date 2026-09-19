package com.example.circle_platform_api.mapstruct;

import com.example.circle_platform_api.entity.Event;
import com.example.circle_platform_api.model.dto.EventCreateDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface EventConvert {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Event createToEntity(EventCreateDTO dto);

}
