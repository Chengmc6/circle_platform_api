package com.example.circle_platform_api.mapstruct;

import com.example.circle_platform_api.entity.CircleCreateApplication;
import com.example.circle_platform_api.model.dto.CircleCreateApplicationDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CircleConvert {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CircleCreateApplication createToEntity(CircleCreateApplicationDTO dto);

}
